package com.common.behavior.complex.ai.impl.action;


import com.common.behavior.complex.ai.abs.BaseAction;
import com.common.behavior.complex.ai.common.EStatus;
import com.common.behavior.complex.ai.ifs.IBehaviour;

public class ActionAttack extends BaseAction {

  @Override
  public EStatus update() {
    System.out.println("ActionAttack 攻击");
    return EStatus.Success;
  }

  @Override
  public void addChild(IBehaviour child) {
  }
}
