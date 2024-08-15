package com.common.factory.behavior.ifs;



import java.util.List;

/**
 * description: com.common.factory.behavior.ifs
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/31
 * version: 1.0
 */
public interface IComposite extends IBehavior {

    void addChild(IBehavior child);

    List<IBehavior> getChildren();

}
