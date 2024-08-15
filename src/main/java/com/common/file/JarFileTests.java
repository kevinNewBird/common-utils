package com.common.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

/**
 * description: com.common.file
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/3/12
 * version: 1.0
 */
public class JarFileTests {

    static Map<String, Map<Locale, MessageFormat>> i18nHolders = new HashMap<>();
    private static final String URL_PREFIX = "i18n/messages_{0}.properties";

    private static final String[] URL_POSTFIX_ARRAY = {"zh", "en"};

    static {
        for (String urlPostfix : URL_POSTFIX_ARRAY) {
            try (InputStream is = JarFileTests.class.getClassLoader()
                    .getResourceAsStream(MessageFormat.format(URL_PREFIX, urlPostfix));){

                Properties props = new Properties();
                props.load(is);
                Locale locale = Locale.forLanguageTag(urlPostfix);
                mergePropertiesToMap(props, locale);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int a = 10;

    private void f() {
        System.out.println("before a:" + a);
        a = 20;
    }
    private void f2() {
        System.out.println("after a:" + a);
    }

    public static void main(String[] args) throws IOException {
        JarFileTests obj = new JarFileTests();
        obj.f();
        obj.f2();

//        Object[] argsToUse = {"1111","3333"};
//        Object[] argsToUse = {};

//        System.out.println(i18nHolders.get("60004").get(Locale.CHINESE).format(null));

    }


    private static void mergePropertiesToMap(Properties props, Locale locale) {
        Enumeration<?> enumeration = props.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            Map<Locale, MessageFormat> localeMap = i18nHolders.getOrDefault(key, new HashMap<Locale, MessageFormat>());
            String format = props.getProperty(key);
            MessageFormat messageFormat = new MessageFormat(format);
            localeMap.put(locale, messageFormat);
            i18nHolders.put(key, localeMap);
        }
    }


}
