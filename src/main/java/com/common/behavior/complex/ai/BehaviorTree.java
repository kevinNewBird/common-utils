package com.common.behavior.complex.ai;


import com.common.behavior.complex.ai.ifs.IBehaviour;

public class BehaviorTree {
  private IBehaviour root;

  public BehaviorTree(IBehaviour root) {
    this.root = root;
  }

  public void tick() {
    root.tick();
  }

  public boolean haveRoot() {
    return root != null ? true : false;
  }

  public void setRoot(IBehaviour inNode) {
    root = inNode;
  }

  public void release() {
    root.release();
  }

}