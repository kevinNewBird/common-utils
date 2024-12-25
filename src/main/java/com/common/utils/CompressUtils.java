package com.common.utils;

import com.common.utils.ssh_pool.SSHConfig;
import com.common.utils.ssh_pool.SSHExecutor;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.MessageFormat;
import java.util.function.Predicate;

/**
 * description: com.common.utils</BR>
 * TIP:
 * 只保留软连接文件，但会抹除软连接（需要单独处理，但可能达不到原来的软连接效果）
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/9/13
 * version: 1.0
 */
public final class CompressUtils {

    private static final char R_AUTH = 'r';

    private static final char W_AUTH = 'w';

    private static final char X_AUTH = 'x';

    private static final char NO_AUTH = '-';

    private CompressUtils() {
    }

    public static void main(String[] args) throws IOException {
        decompressAllByTar("D:\\work-test\\test.tar.gz", "D:\\work-test\\output"
                , innerFile -> innerFile != null && innerFile.getName().toLowerCase().startsWith("python36") && innerFile.getName().toLowerCase().endsWith("tar.gz"));
        // 解压文件，然后分发到远程
//        decompressAllByTar("/root/test.tar.gz", "/root/output", innerFile -> innerFile != null && innerFile.getName().toLowerCase().startsWith("python36") && innerFile.getName().toLowerCase().endsWith("tar.gz"));

        // 上传到远程目录
//        uploadToRemote();
    }

    private static void uploadToRemote() {
        SSHConfig sshConfig = new SSHConfig();
//        sshConfig.setHost("127.0.0.1");
        sshConfig.setHost("10.0.2.54");
        sshConfig.setFtpPort(22);
        sshConfig.setUsername("root");
        sshConfig.setPassword("trsadmin123");
//        SSHExecutor.uploadDirToRemote(sshConfig, "D:\\work-test\\output\\test", "/root");
        // TIP: scp原生命令也是无法保留软连接的（无法保留软连接）
        SSHExecutor.uploadDirToRemote(sshConfig, "/root/output/test", "/root");
    }


    public static void decompressAllByTar(String srcTarFile, String targetDir) throws IOException {
        decompressAllByTar(srcTarFile, targetDir, null);
    }

    /**
     * 解压tar.gz、tgz、tar、tar.bz2
     * description:
     * create by: zhaosong 2024/9/13 10:26
     */
    public static void decompressAllByTar(String srcTarFile, String targetDir, Predicate<File> condition) throws IOException {
        if (StringUtils.isBlank(srcTarFile) || !Files.exists(Paths.get(srcTarFile))
                || !Files.isRegularFile(Paths.get(srcTarFile))) {
            throw new RuntimeException(MessageFormat.format("The compress file [{0}] is empty string or not exists!", srcTarFile));
        }
        String tmpSrcFile = srcTarFile.toLowerCase();
        if (StringUtils.endsWithAny(tmpSrcFile, ".tar.gz", "tgz")) {
            decompressByTGZ(srcTarFile, targetDir, condition);
        } else if (StringUtils.endsWithAny(tmpSrcFile, ".tar.bz2")) {
            decompressByTarBZ(srcTarFile, targetDir, condition);
        } else if (StringUtils.endsWithAny(tmpSrcFile, ".tar")) {
            decompressByTar(srcTarFile, targetDir, condition);
        } else {
            throw new RuntimeException("Unsupported Compressed file type!");
        }
    }


    /**
     * 解压tar.gz或tgz
     * description:
     * create by: zhaosong 2024/9/13 10:07
     *
     * @param srcTarFile
     * @param targetDir
     * @throws IOException
     */
    private static void decompressByTGZ(String srcTarFile, String targetDir, Predicate<File> condition) throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(srcTarFile));
             InputStream gzip = new GzipCompressorInputStream(is);
             TarArchiveInputStream tis = new TarArchiveInputStream(gzip)) {
            doDecompress(tis, targetDir, condition);
        }
    }

    /**
     * 解压tar.bz2
     * description:
     * create by: zhaosong 2024/9/13 10:07
     *
     * @param srcTarFile
     * @param targetDir
     * @throws IOException
     */
    private static void decompressByTarBZ(String srcTarFile, String targetDir, Predicate<File> condition) throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(srcTarFile));
             InputStream bzip = new BZip2CompressorInputStream(is);
             TarArchiveInputStream tis = new TarArchiveInputStream(bzip)) {
            doDecompress(tis, targetDir, condition);
        }
    }

    /**
     * 解压tar文件
     * description:
     * create by: zhaosong 2024/9/13 10:07
     *
     * @param srcTarFile
     * @param targetDir
     * @throws IOException
     */
    private static void decompressByTar(String srcTarFile, String targetDir, Predicate<File> condition) throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(srcTarFile));
             TarArchiveInputStream tis = new TarArchiveInputStream(is)) {
            doDecompress(tis, targetDir, condition);
        }
    }

    /**
     * 解压文件
     * description:
     * create by: zhaosong 2024/9/13 10:23
     *
     * @param tis
     * @param targetDir
     * @throws IOException
     */
    private static void doDecompress(TarArchiveInputStream tis, String targetDir, Predicate<File> condition) throws IOException {
        TarArchiveEntry entry = null;
        while ((entry = tis.getNextEntry()) != null) {
            if (!tis.canReadEntryData(entry)) {
                System.out.println("No Read Auth:" + entry.getName());
                continue;
            }
            File targetFile = new File(targetDir, entry.getName());
            if (entry.isDirectory()) {
                if (!targetFile.isDirectory() && !targetFile.mkdirs()) {
                    // 创建目录失败
                    System.out.println("....");
                }
            } else if (entry.isSymbolicLink()) {
                // 先删除已存在的软连接
                Files.deleteIfExists(targetFile.toPath());
                // 创建软连接
                File realFile = new File(entry.getLinkName());
                Files.createSymbolicLink(targetFile.toPath(), realFile.toPath());
            } else {
                File parentFile = targetFile.getParentFile();
                if (!parentFile.isDirectory() && !parentFile.mkdirs()) {
                    // 创建目录失败
                    System.out.println("....");
                }
                System.out.println(entry.getMode() + " === " + parseFilePermissions(entry.getMode()));
                try (OutputStream os = Files.newOutputStream(targetFile.toPath())) {
                    IOUtils.copy(tis, os);
                }
                Files.setPosixFilePermissions(targetFile.toPath()
                        , PosixFilePermissions.fromString(parseFilePermissions(entry.getMode())));
                if (condition != null && condition.test(targetFile)) {
                    decompressAllByTar(targetFile.getAbsolutePath(), targetFile.getParent());
                }
            }
        }
    }

    /**
     * 解析权限
     * description:
     * create by: zhaosong 2024/12/24 19:02
     *
     * @param unixMode
     * @return
     */
    private static String parseFilePermissions(int unixMode) {
        // 构建文件权限字符串，例如 "rwxr-xr-x"
        StringBuilder permissionsBuilder = new StringBuilder();

        permissionsBuilder
                // 所属者权限
                // (二进制)读权限：100 000 000， 写权限： 010 000 000， 其它：001 000 000
                // (八进制)读权限：0400， 写权限：0200， 其它：0100
                .append(parsePermission(unixMode, 0400, 0200, 0100))
                // 所属组权限
                .append(parsePermission(unixMode, 0040, 0020, 0010))
                // 其它权限
                .append(parsePermission(unixMode, 0004, 0002, 0001));
        return permissionsBuilder.toString();
    }

    /**
     * description:解析各部分权限
     * create by: zhaosong 2024/12/24 18:57
     *
     * @param unixMode: 权限
     * @param read:     读权限
     * @param write:    写权限
     * @param other:    其它权限
     * @return
     */
    private static String parsePermission(int unixMode, int read, int write, int other) {
        StringBuilder partBuilder = new StringBuilder();
        if ((unixMode & read) != 0) {
            partBuilder.append(R_AUTH);
        } else {
            partBuilder.append(NO_AUTH);
        }
        if ((unixMode & write) != 0) {
            partBuilder.append(W_AUTH);
        } else {
            partBuilder.append(NO_AUTH);
        }
        if ((unixMode & other) != 0) {
            partBuilder.append(X_AUTH);
        } else {
            partBuilder.append(NO_AUTH);
        }
        return partBuilder.toString();
    }
}
