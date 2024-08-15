package com.common.factory.xml;

import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/29
 * version: 1.0
 */
public class SimpleErrorHandler implements ErrorHandler {

    private final Logger logger;

    public SimpleErrorHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warning(SAXParseException ex) throws SAXException {
        logger.warn("Ignored XML validation warning", ex);
    }

    @Override
    public void error(SAXParseException ex) throws SAXException {
        throw ex;
    }

    @Override
    public void fatalError(SAXParseException ex) throws SAXException {
        throw ex;
    }
}
