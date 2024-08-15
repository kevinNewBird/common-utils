package com.common.factory;

import com.common.factory.behavior.BehaviorTree;
import com.common.factory.behavior.impl.action.ActionConnectivity;
import com.common.factory.behavior.impl.composite.SequenceImpl;
import com.common.factory.behavior.impl.composite.SelectorImpl;
import com.common.factory.behavior.impl.condition.ConditionIsReadyContext;
import com.common.factory.xml.XmlBeanDefinitionReader;

import java.util.BitSet;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/30
 * version: 1.0
 */
public class Main {

    public static void main(String[] args) throws Exception {
        testBehaviorTree();
//        testXmlReader();
    }

    private static void testBehaviorTree() {
        SelectorImpl root = new SelectorImpl();
        // 生成左叶子节点
        ConditionIsReadyContext left = new ConditionIsReadyContext();
        SequenceImpl right = new SequenceImpl();
        root.addChild(left);
        root.addChild(right);

        // 生成控制节点的节点
        ConditionIsReadyContext ctl_left = new ConditionIsReadyContext();
        SelectorImpl ctl_right = new SelectorImpl();
        right.addChild(ctl_left);
        right.addChild(ctl_right);

        // 生成子选择节点的节点
        ConditionIsReadyContext sle_left = new ConditionIsReadyContext();
        SequenceImpl sle_right = new SequenceImpl();
        ctl_right.addChild(sle_left);
        ctl_right.addChild(sle_right);

        // sle_right控制节点的节点
        ConditionIsReadyContext sle_ctl_left = new ConditionIsReadyContext();
        ActionConnectivity sle_ctl_right = new ActionConnectivity();
        sle_right.addChild(sle_ctl_left);
        sle_right.addChild(sle_ctl_right);

        BehaviorTree behaviorTree = new BehaviorTree(root);
        behaviorTree.tick();
    }

    private static void testXmlReader() throws Exception {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader();
        int count = reader.loadBeanDefinitions(Main.class.getClassLoader().getResourceAsStream("test_cm.xml"));
    }
}
