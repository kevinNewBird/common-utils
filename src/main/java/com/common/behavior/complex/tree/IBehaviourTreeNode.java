package com.common.behavior.complex.tree;

public interface IBehaviourTreeNode {
//  RunStatus status;
//  String nodeName;
//  RenderableNode renderNode;
//  IBehaviourTreeNode parent;

//  boolean Enter(object input);
//
//  boolean Leave(object input);
//
//  boolean Tick(object input, object output);
  IBehaviourTreeNode Clone();


  public RunStatus getStatus() ;

  public void setStatus(RunStatus status);

  public String getNodeName() ;

  public void setNodeName(String nodeName);

  public RenderableNode getRenderNode();

  public void setRenderNode(RenderableNode renderNode);

  public IBehaviourTreeNode getParent();

  public void setParent(IBehaviourTreeNode parent);
}