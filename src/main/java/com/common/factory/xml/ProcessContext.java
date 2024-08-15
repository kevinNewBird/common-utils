package com.common.factory.xml;

import com.common.behavior.complex.ai.BehaviorTree;
import com.common.behavior.complex.ai.BehaviorTreeBuilder;
import com.common.behavior.complex.ai.ifs.IBehaviour;

import java.util.concurrent.ConcurrentHashMap;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public final class ProcessContext {

    private static final ConcurrentHashMap<String, BehaviorTree> REGISTRY = new ConcurrentHashMap<>();


    public static void register(String behaviorName, BehaviorTree instanceObj) {
        REGISTRY.put(behaviorName, instanceObj);
    }

    public static BehaviorTree getBehaviorTree(String behaviorName) {
       return REGISTRY.getOrDefault(behaviorName, new BehaviorTreeBuilder().end());
    }


}
