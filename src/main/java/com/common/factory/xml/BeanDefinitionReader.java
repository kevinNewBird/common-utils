package com.common.factory.xml;

import java.io.IOException;
import java.io.InputStream;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/29
 * version: 1.0
 */
public interface BeanDefinitionReader {

    int loadBeanDefinitions(InputStream resource) throws Exception;

    int loadBeanDefinitions(InputStream... resources) throws Exception;
}
