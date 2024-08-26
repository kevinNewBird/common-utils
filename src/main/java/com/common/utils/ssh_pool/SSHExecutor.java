package com.common.utils.ssh_pool;

import com.common.exceptions.SSHException;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.scp.client.CloseableScpClient;
import org.apache.sshd.scp.client.ScpClient;
import org.apache.sshd.scp.client.ScpClientCreator;
import org.apache.sshd.scp.client.ScpRemote2RemoteTransferHelper;
import org.apache.sshd.scp.server.ScpCommandFactory;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * description: cn.com.mina.pool
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/7/14
 * version: 1.0
 */
public class SSHExecutor {

    private static Logger logger = LoggerFactory.getLogger(SSHExecutor.class);

    // 注意：针对于centos, 需要指定-Djava.security.egd=file:/dev/urandom
    // eg.  java -Djava.security.egd=file:/dev/urandom -jar xxx.jar
    // 参考地址：https://blog.csdn.net/lijingjingchn/article/details/88244462

    public static SSHResult exec(SSHConfig config, String cmdLine) {
        return exec(config, cmdLine, Collections.emptyMap());
    }

    /**
     * description: 执行shell命令
     * create by: zhaosong 2023/9/20 16:33
     *
     * @param config
     * @param cmdLine
     * @param autoResponse
     * @return
     */
    public static SSHResult exec(SSHConfig config, String cmdLine, Map<String, String> autoResponse) {

        ClientSession session = null;
        ChannelExec channel = null;
        InputStream is = null;
        OutputStream os = null;
        int statusCode = -1;
        try {
            if (Objects.isNull(config)) {
                throw new SSHException("服务器配置为空，请联系管理员确认！");
            }
            if (StringUtils.isBlank(cmdLine)) {
                throw new SSHException("传入shell指令为空，请联系管理员确认！");
            }

            session = SSHPool.getInstance().borrowObject(config);
            channel = session.createExecChannel(cmdLine);
            channel.setUsePty(true);
            channel.open().verify(20, TimeUnit.SECONDS);
            is = channel.getInvertedOut();
            os = channel.getInvertedIn();
            byte[] cache = new byte[1024];
            int len = 0;
            long start = System.currentTimeMillis();
            StringBuilder ret = new StringBuilder();
            while (true) {
                if (config.getExecTimeout() > 0 && System.currentTimeMillis() - start > config.getExecTimeout()) {
                    throw new SSHException(MessageFormat.format("执行ssh命令超时，超时配置为{0}ms", config.getExecTimeout()));
                }
                while (is.available() > 0) {
                    len = is.read(cache, 0, 1024);
                    if (len < 0) {
                        break;
                    }
                    String content = new String(cache, 0, len);
                    ret.append(content);
                }
                if (channel.isClosed()) {
                    if (is.available() > 0) {
                        continue;
                    }
                    break;
                }
                doAutoResponse(os, autoResponse, ret);
                TimeUnit.SECONDS.sleep(1);
            }

            statusCode = Objects.nonNull(channel.getExitStatus()) ? channel.getExitStatus() : -1;
            return new SSHResult.SSHResultHolder().code(statusCode).message(ret.toString()).build();
        } catch (Exception ex) {
            logger.error("执行ssh命令发生错误!", ex);
            return new SSHResult.SSHResultHolder().code(statusCode).message(ex.getMessage()).build();
        } finally {
            try {
                if (Objects.nonNull(os)) {
                    os.close();
                }
                if (Objects.nonNull(is)) {
                    is.close();
                }
                if (Objects.nonNull(channel) && channel.isOpen()) {
                    channel.close();
                }
                if (Objects.nonNull(session) && session.isOpen()) {
                    SSHPool.getInstance().returnObject(config, session);
                }
            } catch (IOException e) {
                logger.error("归还SSH连接的相关资源发生异常!", e);
            }
        }
    }

    private static void doAutoResponse(OutputStream os, Map<String, String> autoResponse, StringBuilder ret) {
        if (Objects.nonNull(autoResponse) && StringUtils.isNotBlank(ret.toString().trim())) {
            for (Map.Entry<String, String> linePair : autoResponse.entrySet()) {
                String flag = linePair.getKey();
                String pairResp = linePair.getValue();
                if (ret.toString().trim().endsWith(flag)) {
                    try {
                        os.write(pairResp.getBytes());
                        os.flush();
                        ret.append(pairResp);
                    } catch (IOException e) {
                        System.out.println("auto response occurs error!!!" + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * description: 上传本地文件到远端
     * create by: zhaosong 2023/9/20 16:33
     *
     * @param config
     * @param sourceFullName
     * @param remoteFullName
     * @return
     */
    public static SSHResult uploadToRemote(SSHConfig config, String sourceFullName, String remoteFullName) {
        ClientSession session = null;
        SftpFileSystem fs = null;
        try {
            session = SSHPool.getInstance().borrowObject(config);
            fs = SftpClientFactory.instance().createSftpFileSystem(session);

            File remoteFile = new File(remoteFullName.trim());
            Path remoteRoot = fs.getDefaultDir().resolve(remoteFile.getParent());
            if (!Files.exists(remoteRoot)) {
                Files.createDirectories(remoteRoot);
            }

            Path remotePath = remoteRoot.resolve(remoteFile.getName());
            Files.deleteIfExists(remotePath);
            Files.copy(Paths.get(sourceFullName), remotePath);

            logger.info(MessageFormat.format("upload {0} to {1} success!", sourceFullName, remoteFullName));
            return new SSHResult.SSHResultHolder().code(0).message("上传成功！").build();
        } catch (Throwable ex) {
            logger.error(MessageFormat.format("上传文件：{0}失败！", sourceFullName), ex);
            return new SSHResult.SSHResultHolder().code(-1).message(ex.getMessage()).build();
        } finally {

            try {
                if (Objects.nonNull(fs) && fs.isOpen()) {
                    fs.close();
                }
                if (Objects.nonNull(session) && session.isOpen()) {
                    SSHPool.getInstance().returnObject(config, session);
                }
            } catch (IOException e) {
                logger.error("上传文件，归还SSH连接相关资源失败！", e);
            }
        }
    }

    /**
     * description: 下载远程文件到本地
     * create by: zhaosong 2023/9/20 16:32
     *
     * @param config
     * @param remoteFullName
     * @param localFullName
     */
    public static SSHResult downloadToLocal(SSHConfig config, String remoteFullName, String localFullName) {
        ClientSession session = null;
        SftpClient sc = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            session = SSHPool.getInstance().borrowObject(config);
            sc = SftpClientFactory.instance().createSftpClient(session);
            is = sc.read(remoteFullName);
            os = Files.newOutputStream(Paths.get(localFullName));
            IoUtils.copy(is, os);
            logger.info(MessageFormat.format("download {0} to {1} success!", remoteFullName, localFullName));
            return new SSHResult.SSHResultHolder().code(0).message("下载成功！").build();
        } catch (Throwable ex) {
            logger.error(MessageFormat.format("下载文件：{0}失败！", remoteFullName), ex);
            return new SSHResult.SSHResultHolder().code(-1).message(ex.getMessage()).build();
        } finally {
            try {
                if (Objects.nonNull(os)) {
                    os.close();
                }
                if (Objects.nonNull(is)) {
                    is.close();
                }
                if (Objects.nonNull(sc) && sc.isOpen()) {
                    sc.close();
                }
                if (Objects.nonNull(session) && session.isOpen()) {
                    SSHPool.getInstance().returnObject(config, session);
                }
            } catch (Throwable e) {
                logger.error("下载文件，归还SSH连接相关资源失败！", e);
            }
        }

    }


    /**
     * 上传本地目录到远程
     * description:
     * create by: zhaosong 2024/8/22 16:50
     *
     * @param config
     * @param sourceDir
     * @param remoteDir
     * @return
     */
    public static SSHResult uploadDirToRemote(SSHConfig config, String sourceDir, String remoteDir) {
        try (ClientSession session = SSHPool.getInstance().borrowObject(config);
             CloseableScpClient client = CloseableScpClient.singleSessionInstance(ScpClientCreator.instance().createScpClient(session))) {

            client.upload(Paths.get(sourceDir), remoteDir, ScpClient.Option.TargetIsDirectory);
            return new SSHResult.SSHResultHolder().code(0).message("上传成功！").build();
        } catch (Throwable ex) {
            logger.error(MessageFormat.format("上传{0}目录下所有文件失败！", sourceDir), ex);
            return new SSHResult.SSHResultHolder().code(-1).message(ex.getMessage()).build();
        }
    }

    /**
     * 下载远程目录到本地
     * description:
     * create by: zhaosong 2024/8/22 16:49
     *
     * @param config
     * @param remoteDir
     * @param localDir
     * @return
     */
    public static SSHResult downloadDirToRemote(SSHConfig config, String remoteDir, String localDir) {
        try (ClientSession session = SSHPool.getInstance().borrowObject(config);
             CloseableScpClient client = CloseableScpClient.singleSessionInstance(ScpClientCreator.instance().createScpClient(session))) {

            client.download(remoteDir, localDir, ScpClient.Option.TargetIsDirectory);
            return new SSHResult.SSHResultHolder().code(0).message("下载成功！").build();
        } catch (Throwable ex) {
            logger.error(MessageFormat.format("下载{0}目录下所有文件失败！", remoteDir), ex);
            return new SSHResult.SSHResultHolder().code(-1).message(ex.getMessage()).build();
        }
    }

    /**
     * 模拟scp两个远程连接数据互传
     * description:
     * create by: zhaosong 2024/8/22 18:14
     *
     * @param srcConfig
     * @param srcRemoteDir
     * @param targetRemoteDir
     * @return
     */
    public static SSHResult scpRemote2Remote(SSHConfig srcConfig, SSHConfig targetConfig, String srcRemoteDir, String targetRemoteDir) {
        try (ClientSession srcSession = SSHPool.getInstance().borrowObject(srcConfig);
             ClientSession targetSession = SSHPool.getInstance().borrowObject(targetConfig)) {

            ScpRemote2RemoteTransferHelper channel = new ScpRemote2RemoteTransferHelper(srcSession, targetSession);
            channel.transferDirectory(srcRemoteDir, targetRemoteDir, false);
            return new SSHResult.SSHResultHolder().code(0).message("下载成功！").build();
        } catch (Throwable ex) {
            logger.error(MessageFormat.format("下载{0}目录下所有文件失败！", srcRemoteDir), ex);
            return new SSHResult.SSHResultHolder().code(-1).message(ex.getMessage()).build();
        }
    }

}
