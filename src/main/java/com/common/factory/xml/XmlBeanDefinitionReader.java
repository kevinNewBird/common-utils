package com.common.factory.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import java.io.InputStream;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/29
 * version: 1.0
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private static final Logger logger = LoggerFactory.getLogger(DelegateEntityResolver.class);

    private boolean namespaceAware = true;

    private DatabaseDocumentLoader documentLoader = new DefaultDatabaseDocumentLoader();

    private EntityResolver entityResolver;

    private ErrorHandler errorHandler = new SimpleErrorHandler(logger);

    public void setNamespaceAware(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }

    public boolean isNamespaceAware() {
        return this.namespaceAware;
    }

    public void setDocumentLoader(DatabaseDocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
    }


    public EntityResolver getEntityResolver() {
        if (this.entityResolver == null) {
            this.entityResolver = new DelegateEntityResolver();
        }
        return this.entityResolver;
    }

    public void setEntityResolver(EntityResolver entityResolver) {

        this.entityResolver = entityResolver;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }


    public Document doLoadDocument(InputSource inputSource) throws Exception {
        return this.documentLoader.loadDocument(inputSource, getEntityResolver()
                , this.errorHandler, this.namespaceAware);
    }

    @Override
    public int loadBeanDefinitions(InputStream resource) throws Exception {
        try {
            InputSource inputSource = new InputSource(resource);
            return doLoadBeanDefinitions(inputSource);
        } finally {
            resource.close();
        }

    }


    protected int doLoadBeanDefinitions(InputSource inputSource) throws Exception {
        // 解析安装配置文件
        Document doc = doLoadDocument(inputSource);

        // 处理成行为树结构
        BeanDefinitionDocumentReader reader = createBeanDefinitionDocumentReader();
        reader.registerBeanDefinitions(doc);
        return 0;
    }

    protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader(){
        return new DefaultBeanDefinitionDocumentReader();
    }


}
