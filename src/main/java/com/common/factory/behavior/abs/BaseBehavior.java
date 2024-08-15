package com.common.factory.behavior.abs;

import com.common.factory.behavior.BehaviorTree;
import com.common.factory.behavior.enums.PStatus;
import com.common.factory.behavior.ifs.IBehavior;

/**
 * description: com.common.factory.behavior.abs
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public abstract class BaseBehavior implements IBehavior {

    protected PStatus status;

    public BaseBehavior() {
        this.status = PStatus.Invalid;
    }

    @Override
    public PStatus tick(BehaviorTree tree) {
        if (status != PStatus.Running) {
            onInitialize(status);
        }

        status = process(tree);

        if (status != PStatus.Running) {
            onTerminate(status);
        }

        return status;
    }

    @Override
    public void onInitialize(PStatus status) {

    }

    @Override
    public PStatus process(BehaviorTree tree) {
        return PStatus.Success;
    }

    @Override
    public void onTerminate(PStatus status) {

    }
}
