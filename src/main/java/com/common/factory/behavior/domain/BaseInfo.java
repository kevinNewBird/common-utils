package com.common.factory.behavior.domain;

/**
 * description: com.common.factory.behavior.domain
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public abstract class BaseInfo {

    private ServerInfoWrapper serverWrapper;

    public ServerInfoWrapper getServerWrapper() {
        return serverWrapper;
    }

    public void setServerWrapper(ServerInfoWrapper serverWrapper) {
        this.serverWrapper = serverWrapper;
    }
}
