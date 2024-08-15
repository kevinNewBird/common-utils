package com.common.factory.behavior.domain;

import java.util.Map;

/**
 * description: com.common.factory.behavior.domain
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public class ServerInfoWrapper {

    // key为唯一标识，用于区分不同服务器实例对象
    private Map<String, ServerInfo> serverList;

    public Map<String, ServerInfo> getServerList() {
        return serverList;
    }

    public void setServerList(Map<String, ServerInfo> serverList) {
        this.serverList = serverList;
    }
}
