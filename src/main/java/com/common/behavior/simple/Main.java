package com.common.behavior.simple;

/**
 * description: com.common.behavior
 * company: 北京海量数据有限公司
 * create by: zhaosong 2023/12/11
 * version: 1.0
 */
public class Main {


    /**
     *
     * description: 行为树
     * 参考博客： https://blog.51cto.com/u_16175526/6718621
     * create by: zhaosong 2023/12/11 15:22
     * @param args
     */
    public static void main(String[] args) {
        RootNode rootNode = new RootNode("Root");

        CompositeNode compositeNode = new CompositeNode("Composite");
        rootNode.addChild(compositeNode);

        LeafNode leafNode1 = new LeafNode("leaf 1");
        compositeNode.addChild(leafNode1);
        LeafNode leafNode2 = new LeafNode("leaf 2");
        compositeNode.addChild(leafNode2);

        rootNode.execute();
    }

}
