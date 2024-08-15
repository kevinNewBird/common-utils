package com.common.behavior.simple;

/**
 * description: 执行行为树
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/12/11
 * version: 1.0
 */
public class LeafNode extends BehaviorNode{


    public LeafNode(String name) {
        super(name);
    }

    @Override
    public void execute() {
        System.out.println("Executing leaf node: " + name);
    }

}
