package com.common.behavior.complex.ai.impl.action;


import com.common.behavior.complex.ai.abs.BaseAction;
import com.common.behavior.complex.ai.common.EStatus;
import com.common.behavior.complex.ai.ifs.IBehaviour;

public class ActionPatrol extends BaseAction {

  @Override
  public EStatus update() {
    System.out.println("ActionPatrol 巡逻");


    return EStatus.Success;
  }

  @Override
  public void addChild(IBehaviour child) {
  }
}
