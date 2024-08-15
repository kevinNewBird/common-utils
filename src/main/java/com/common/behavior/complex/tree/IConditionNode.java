package com.common.behavior.complex.tree;
/************************************************************************/
/* 条件节点                                                             */  
/************************************************************************/  
public interface IConditionNode  
{  
    boolean externalCondition();
    void setNodeName(String nodeName);
    void getNodeName();

}  