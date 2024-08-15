package com.common.behavior.simple;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 构建行为树（组合模式）
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/12/11
 * version: 1.0
 */
public class CompositeNode extends BehaviorNode{

    private List<BehaviorNode> children;

    public CompositeNode(String name) {
        super(name);
        children = new ArrayList<>();
    }

    public void addChild(BehaviorNode child) {
        children.add(child);
    }

    public void removeChild(BehaviorNode child) {
        children.remove(child);
    }

    @Override
    public void execute() {
        for (BehaviorNode child : children) {
            child.execute();
        }
    }
}
