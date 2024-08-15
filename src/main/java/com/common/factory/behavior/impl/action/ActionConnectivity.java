package com.common.factory.behavior.impl.action;

import com.common.factory.behavior.BehaviorTree;
import com.common.factory.behavior.abs.BaseAction;
import com.common.factory.behavior.enums.PStatus;

/**
 * description: com.common.factory.behavior.impl.action
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public class ActionConnectivity extends BaseAction {

    @Override
    public PStatus process(BehaviorTree tree) {
        System.out.println("action");
        return PStatus.Success;
    }
}
