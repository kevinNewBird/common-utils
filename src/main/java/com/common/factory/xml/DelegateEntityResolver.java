package com.common.factory.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/29
 * version: 1.0
 */
public class DelegateEntityResolver implements EntityResolver {

    public static final String XSD_SUFFIX = ".xsd";

    private final EntityResolver xsdResolver;

    public DelegateEntityResolver() {
        this.xsdResolver = new PluggableXsdResolver();
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId != null) {
            if (systemId.endsWith(XSD_SUFFIX)) {
                return this.xsdResolver.resolveEntity(publicId, systemId);
            }
        }
        return null;
    }
}
