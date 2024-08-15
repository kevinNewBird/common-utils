package com.common.factory.xml;

import java.io.InputStream;
import java.util.Objects;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/29
 * version: 1.0
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    @Override
    public int loadBeanDefinitions(InputStream... resources) throws Exception {
        if (Objects.isNull(resources)) {
            throw new IllegalArgumentException("Resource array must not be null");
        }
        int count = 0;
        for (InputStream resource : resources) {
            count += loadBeanDefinitions(resource);
        }
        return 0;
    }
}
