package com.common.factory.behavior.domain;

/**
 * description: com.common.factory.behavior.domain
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public class ServerInfo {

    private String host;

    private int ftpPort;

    private String username;

    private String password;

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
}
