package com.common.utils.ssh_pool;

/**
 * description: cn.com.mina.pool
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/7/14
 * version: 1.0
 */
public class SSHConfig {

    private String host;

    private int ftpPort;

    private String username;

    private String password;

    //单位：ms (默认30s), 校验超时时间（用于连接池申请连接时使用）
    private long timeout = 30_000;

    // 单位：ms (<=0表示阻塞等待，>0超时退出)
    private long execTimeout = 0;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getExecTimeout() {
        return execTimeout;
    }

    public void setExecTimeout(long execTimeout) {
        this.execTimeout = execTimeout;
    }
}
