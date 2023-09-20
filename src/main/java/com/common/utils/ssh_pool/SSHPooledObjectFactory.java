package com.common.utils.ssh_pool;

import com.common.exceptions.SSHException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.sshd.client.ClientBuilder;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.core.CoreModuleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Objects;

/**
 * description: cn.com.mina.pool
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/7/14
 * version: 1.0
 */
public class SSHPooledObjectFactory extends BaseKeyedPooledObjectFactory<SSHConfig, ClientSession> {

    private static Logger logger = LoggerFactory.getLogger(SSHPooledObjectFactory.class);

    private static final String VALIDATE_COMMAND = "echo '1'";

    public SSHPooledObjectFactory() {
    }

    @Override
    public ClientSession create(SSHConfig config) throws Exception {
        try {
            SshClient client = ClientBuilder.builder().build();
            // 空闲时间设置为5min,默认为10min。避免阻塞等待时，时间过短，导致连接被强行释放
            CoreModuleProperties.IDLE_TIMEOUT.set(client, Duration.ofMillis(5L));
            client.start();

            long timeout = config.getTimeout() > 0 ? config.getTimeout() : Long.MAX_VALUE;
            ClientSession session = client.connect(config.getUsername(), config.getHost(), config.getFtpPort())
                    .verify(timeout).getSession();
            session.addPasswordIdentity(config.getPassword());
            boolean success = session.auth().verify(timeout).isSuccess();
            if (success) {
                return session;
            } else {
                session.close();
                throw new SSHException(MessageFormat.format("authentication failed after {0} mill seconds", timeout));
            }
        } catch (Throwable ex) {
            if (StringUtils.contains(ex.getMessage(), "authentication")) {
                throw new SSHException("用户名或密码错误！");
            }
            if (StringUtils.contains(ex.getMessage(), "specified timeout")) {
                throw new SSHException("无效的地址或端口");
            }
            throw new SSHException(ex.getMessage(), ex);
        }

    }

    @Override
    public PooledObject<ClientSession> wrap(ClientSession session) {
        return new DefaultPooledObject<>(session);
    }

    @Override
    public boolean validateObject(SSHConfig config, PooledObject<ClientSession> poolObj) {
        ClientSession session = poolObj.getObject();
        try (ChannelExec channel = session.createExecChannel(VALIDATE_COMMAND)) {
            channel.open();
            channel.waitFor(Collections.singletonList(ClientChannelEvent.CLOSED), 10_000L);
            return Objects.nonNull(channel.getExitStatus()) && channel.getExitStatus() == 0;
        } catch (Exception e) {
            logger.warn(MessageFormat.format("[IP:{0}] SSH session validate error!", config.getHost()), e);
            poolObj.markAbandoned();
            return false;
        }
    }

    @Override
    public void destroyObject(SSHConfig key, PooledObject<ClientSession> pool) throws Exception {
        ClientSession session = pool.getObject();
        if (Objects.nonNull(session) && session.isOpen()) {
            SshClient client = (SshClient) session.getFactoryManager();
            if (Objects.nonNull(client) && client.isOpen()) {
                client.stop();
                client.close();
            }
            session.close();
        }
    }
}
