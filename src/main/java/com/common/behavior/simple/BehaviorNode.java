package com.common.behavior.simple;

import com.common.behavior.simple.enums.NodeStatus;

/**
 * description: 定义行为树节点
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/12/11
 * version: 1.0
 */
public abstract class BehaviorNode {

    protected String name;

    protected NodeStatus status;

    public BehaviorNode(String name) {
        this.name = name;
        this.status = NodeStatus.READY;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public abstract void execute();
}
