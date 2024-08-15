package com.common.behavior.complex.ai.impl.condition;


import com.common.behavior.complex.ai.abs.BaseCondition;
import com.common.behavior.complex.ai.common.EStatus;
import com.common.behavior.complex.ai.ifs.IBehaviour;

public class ConditionIsEnemyDead extends BaseCondition {

  @Override
  public EStatus update() {
    int random = getRandom();
    if (random < 60) {
      System.out.println("Enemy Is Dead");
      return !isNegation() ? EStatus.Success : EStatus.Failure;
    } else {
      System.out.println("Enemy is Not Dead");
      return !isNegation() ? EStatus.Failure : EStatus.Success;
    }

  }

  @Override
  public void addChild(IBehaviour child) {
  }
}
