package com.common.behavior.complex;

import com.common.behavior.complex.ai.BehaviorTree;
import com.common.behavior.complex.ai.BehaviorTreeBuilder;
import com.common.behavior.complex.ai.impl.action.ActionAttack;
import com.common.behavior.complex.ai.impl.action.ActionPatrol;
import com.common.behavior.complex.ai.impl.action.ActionRunaway;
import com.common.behavior.complex.ai.impl.composite.SelectorImpl;
import com.common.behavior.complex.ai.impl.composite.SequenceImpl;
import com.common.behavior.complex.ai.impl.condition.ConditionIsHealthLow;
import com.common.behavior.complex.ai.impl.condition.ConditionIsSeeEnemy;


public class Main {


  /**
   * 参考博客： https://blog.csdn.net/cmqwan/article/details/80453352
   * description:
   * create by: zhaosong 2023/12/11 16:39
   * @param args
   */
    public static void main(String[] args) {
        BehaviorTreeBuilder builder = new BehaviorTreeBuilder();
        BehaviorTree behaviorTree =
                builder.addBehaviour(new SelectorImpl())
                        .addBehaviour(new SequenceImpl())
                        .addBehaviour(new ConditionIsSeeEnemy())
                        .back()
                        .addBehaviour(new SelectorImpl())
                        .addBehaviour(new SequenceImpl())
                        .addBehaviour(new ConditionIsHealthLow())
                        .back()
                        .addBehaviour(new ActionRunaway())
                        .back()
                        .back()
                        .addBehaviour(new ActionAttack())
                        .back()
                        .back()
                        .back()
                        .addBehaviour(new ActionPatrol())
                        .end();

        //模拟执行行为树
        for (int i = 0; i < 10; ++i) {
            behaviorTree.tick();
            System.out.println("--------------" + i + "------------");
        }

        System.out.println("pause ");
    }
}
