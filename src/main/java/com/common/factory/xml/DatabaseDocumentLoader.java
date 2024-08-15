package com.common.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/29
 * version: 1.0
 */
public interface DatabaseDocumentLoader {


    Document loadDocument(InputSource inputSource, EntityResolver entityResolver
            , ErrorHandler errorHandler, boolean namespaceAware) throws Exception;
}
