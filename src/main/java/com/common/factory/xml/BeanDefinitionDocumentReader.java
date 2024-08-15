package com.common.factory.xml;

import org.w3c.dom.Document;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/30
 * version: 1.0
 */
public interface BeanDefinitionDocumentReader {

    void registerBeanDefinitions(Document doc);
}
