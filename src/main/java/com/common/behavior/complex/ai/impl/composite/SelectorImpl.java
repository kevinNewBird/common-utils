package com.common.behavior.complex.ai.impl.composite;

import com.common.behavior.complex.ai.abs.BaseComposite;
import com.common.behavior.complex.ai.common.EStatus;
import com.common.behavior.complex.ai.ifs.IBehaviour;
import com.common.behavior.complex.ai.ifs.composite.ISelector;
import com.common.behavior.complex.ai.ifs.composite.ISequence;


import java.util.Iterator;

public class SelectorImpl extends BaseComposite implements ISelector {

  private IBehaviour currChild;

  @Override
  public EStatus update() {
    Iterator<IBehaviour> iterator = getChildren().iterator();
    if (iterator.hasNext()) {
      while (true) {
        currChild = iterator.next();
        EStatus s = currChild.tick();
        //如果执行成功了就继续执行，否则返回
        if (s != EStatus.Failure)
          return s;
        if (!iterator.hasNext())
          return EStatus.Failure;
      }
    }
    return EStatus.Invalid;  //循环意外终止
  }
}
