package com.common.factory.behavior.impl.composite;

import com.common.factory.behavior.BehaviorTree;
import com.common.factory.behavior.abs.BaseComposite;
import com.common.factory.behavior.enums.PStatus;
import com.common.factory.behavior.ifs.IBehavior;

import java.util.Iterator;

/**
 * description: com.common.factory.behavior.impl.composite
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public class SelectorImpl extends BaseComposite {

    @Override
    public PStatus process(BehaviorTree tree) {
        Iterator<IBehavior> iterator = getChildren().iterator();
        while (iterator.hasNext()) {
            IBehavior currChild = iterator.next();
            PStatus status = currChild.tick(tree);
            // 没有失败，继续往下执行流程
            if (status != PStatus.Failure) {
                continue;
            }else {
                return status;
            }
        }
        return PStatus.Invalid;
    }

}
