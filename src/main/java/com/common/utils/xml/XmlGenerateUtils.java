package com.common.utils.xml;

import cn.hutool.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;

/**
 * description: com.common.utils.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/25
 * version: 1.0
 */
public class XmlGenerateUtils {

    private static final Logger logger = LoggerFactory.getLogger(XmlGenerateUtils.class);

    public static String generateClusterConfigXml() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities",false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,"");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA,"");
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            doc.setXmlVersion("1.0");
            doc.setXmlStandalone(true);

            Element rootEle = doc.createElement("ROOT");
            Element clusterEle = doc.createElement("CLUSTER");

            // 构建Cluster子节点
            buildClusterChildEle(doc, clusterEle);

            // 根节点装载Cluster
            rootEle.appendChild(clusterEle);

            doc.appendChild(rootEle);

            TransformerFactory transfer = TransformerFactory.newInstance();
            transfer.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transfer.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            transfer.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transfer.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");

            DOMSource dom = new DOMSource(doc);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            transformer.transform(dom, new StreamResult(bos));

            return bos.toString();
        } catch (Exception e) {
            logger.error("生成xml失败！", e);
            throw new IllegalStateException("generate xml error!");
        }
    }

    private static void buildClusterChildEle(Document doc, Element clusterEle) {
        Element param = doc.createElement("PARAM");
        param.setAttribute("name","test");
        param.setAttribute("value","ssss");
        clusterEle.appendChild(param);
    }
}
