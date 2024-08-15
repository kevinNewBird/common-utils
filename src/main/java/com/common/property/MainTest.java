package com.common.property;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * description: com.common.property
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/6
 * version: 1.0
 */
public class MainTest {

    public static void main(String[] args) throws JsonProcessingException {
        BigDecimal number = new BigDecimal("1.3");
        System.out.println(number.doubleValue());

        BeanDefinitionHolder bd = new BeanDefinitionHolder();

        // 属性1
        PropertyValue prop = new PropertyValue();
        prop.setName("a");
        prop.setValue(new TypeStringValue("a111"));


        // 属性2
        PropertyValue prop2 = new PropertyValue();
        prop2.setName("b");
        ManagedList<PropertyValue> propList = new ManagedList<>();
        PropertyValue prop3 = new PropertyValue();
        prop3.setName("b->c");
        prop3.setValue(new TypeStringValue("c333"));
        propList.add(prop3);
        prop2.setValue(propList);

        bd.getPropertyValueList().add(prop);
        bd.getPropertyValueList().add(prop2);


        System.out.println(new ObjectMapper().writeValueAsString(bd));
//        System.out.println(JSON.toJSONString(bd));
    }
}
