package com.common.factory.behavior.impl.condition;

import com.common.factory.behavior.BehaviorTree;
import com.common.factory.behavior.abs.BaseCondition;
import com.common.factory.behavior.domain.ServerInfo;
import com.common.factory.behavior.enums.PStatus;

import java.util.Map;

/**
 * description: com.common.factory.behavior.impl.condition
 * 是否准备好了安装的上下文参数，如果没有准备，返回前端参数要求
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public class ConditionIsReadyContext extends BaseCondition {

    @Override
    public PStatus process(BehaviorTree tree) {
        while (true) {

//            boolean isReady = tree.get;
            boolean isReady = true;
            if (isReady) {
                return PStatus.Success;
            }
        }
    }

    private boolean checkConnectivity(Map<String, ServerInfo> serverList) {
        for (Map.Entry<String, ServerInfo> entry : serverList.entrySet()) {
            String instanceName = entry.getKey();
            ServerInfo serverInfo = entry.getValue();
        }
        return true;
    }
}
