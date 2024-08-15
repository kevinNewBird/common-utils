package com.common.introspector;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;

/**
 * description: com.common.introspector
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/8
 * version: 1.0
 */
public class IntroSpectorTests {

    public static void main(String[] args) throws IntrospectionException {
        System.out.println(Locale.getDefault().getLanguage());
        System.out.println(Locale.forLanguageTag(Locale.getDefault().getLanguage()));
        System.out.println(Locale.CHINESE+"---"+Locale.ENGLISH);
        BeanInfo beanInfo = Introspector.getBeanInfo(Animal.class, Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            System.out.printf("datatype: %s, name: %s\n",pd.getPropertyType(),pd.getName());
            Type returnType = pd.getReadMethod().getGenericReturnType();
            if (returnType instanceof ParameterizedType) {
                Type[] genericTypes = ((ParameterizedType) returnType).getActualTypeArguments();
                System.out.println(genericTypes[0].getTypeName());
            }

            System.out.println("-------------------------------------------------");
        }
    }
}
