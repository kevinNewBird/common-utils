package com.common.property;

import java.util.ArrayList;
import java.util.List;

/**
 * description: com.common.property
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/6
 * version: 1.0
 */
public class BeanDefinitionHolder {

    private String clazz;

    private List<PropertyValue> propertyValueList;

    public String getClazz() {
        return clazz;
    }

    public BeanDefinitionHolder() {
        this.clazz="parent";
        propertyValueList = new ArrayList<>();
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public void setPropertyValueList(List<PropertyValue> propertyValueList) {
        this.propertyValueList = propertyValueList;
    }
}
