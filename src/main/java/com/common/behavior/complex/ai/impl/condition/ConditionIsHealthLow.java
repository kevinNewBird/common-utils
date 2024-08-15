package com.common.behavior.complex.ai.impl.condition;


import com.common.behavior.complex.ai.abs.BaseCondition;
import com.common.behavior.complex.ai.common.EStatus;
import com.common.behavior.complex.ai.ifs.IBehaviour;

public class ConditionIsHealthLow extends BaseCondition {

  @Override
  public EStatus update() {
    int random = getRandom();
    if (random < 70) {
      System.out.println("Health is low");
      return !isNegation() ? EStatus.Success : EStatus.Failure;
    } else {
      System.out.println("Health is Not low");
      return !isNegation() ? EStatus.Failure : EStatus.Success;
    }

  }

  @Override
  public void addChild(IBehaviour child) {
  }
}
