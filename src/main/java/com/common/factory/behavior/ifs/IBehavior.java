package com.common.factory.behavior.ifs;

import com.common.factory.behavior.BehaviorTree;
import com.common.factory.behavior.enums.PStatus;

/**
 * description: com.common.factory.behavior.ifs
 * Behavior接收是所有行为树节点的核心
 * 规定：
 * 1.行为树的核心处理方法tick，对行为树的共性行为做一个封装
 *    - onInitialize 初始化方法，所有的节点进入前首先调用一次该方法
 *    - process方法，实际执行的逻辑方法
 *    - onTerminate 终止方法，当行为不在处于运行状态时，调用该方法
 * 2.行为树的上下文对象，set
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public interface IBehavior {

    PStatus tick(BehaviorTree tree);

    void onInitialize(PStatus status);

    PStatus process(BehaviorTree tree);

    void onTerminate(PStatus status);



}
