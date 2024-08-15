package com.common.factory.behavior.abs;

import com.common.factory.behavior.ifs.IBehavior;
import com.common.factory.behavior.ifs.IComposite;

import java.util.ArrayList;
import java.util.List;

/**
 * description: com.common.factory.behavior.abs
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public abstract class BaseComposite extends BaseBehavior implements IComposite {

    protected List<IBehavior> children = new ArrayList<>();

    @Override
    public void addChild(IBehavior child) {
        children.add(child);
    }

    @Override
    public List<IBehavior> getChildren() {
        return this.children;
    }
}
