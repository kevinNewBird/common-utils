package com.common.utils.xml;

import org.w3c.dom.Document;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * description: com.common.utils.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/25
 * version: 1.0
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String content = XmlGenerateUtils.generateClusterConfigXml();
        try (FileOutputStream fos = new FileOutputStream("test_cm.xml")){
            fos.write(content.getBytes());
        }

        Document doc = XmlParseUtils.parse(new FileInputStream("D:\\work\\mashibing-lessons-pure\\common-utils\\common-utils\\test_cm.xml"));

        System.out.println("11111");
    }
}
