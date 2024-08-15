package com.common.factory.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: com.common.xml
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/1/30
 * version: 1.0
 */
public class PluggableXsdResolver implements EntityResolver {

    private static final Logger logger = LoggerFactory.getLogger(PluggableXsdResolver.class);

    private static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/vastdata.schemas";

    private final String schemaMappingsLocation;

    private volatile Map<String, String> schemaMappings;

    public PluggableXsdResolver() {
        this.schemaMappingsLocation = DEFAULT_SCHEMA_MAPPINGS_LOCATION;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Trying to resolve XML entity with public ID [" + publicId
                    + "] and system ID [" + systemId + "]");
        }

        if (systemId != null) {
            String resourceLocation = getSchemaMappings().get(systemId);
            if (resourceLocation == null && systemId.startsWith("https:")) {
                resourceLocation = getSchemaMappings().get("http:" + systemId.substring(6));
            }
            if (resourceLocation != null) {
                try {
                    InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourceLocation);
                    InputSource source = new InputSource(is);
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Found XML schema [" + systemId + "] in classpath: " + resourceLocation);
                    }
                    return source;
                } catch (Exception ex) {
                    logger.error("Could not find XML schema [" + systemId + "]: " + resourceLocation, ex);
                }
            }
        }

        return null;
    }

    private Map<String, String> getSchemaMappings() {
        Map<String, String> schemaMappings = this.schemaMappings;
        if (schemaMappings == null) {
            synchronized (this) {
                schemaMappings = this.schemaMappings;
                if (schemaMappings == null) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
                    }
                    try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.schemaMappingsLocation)) {
                        Properties mappings = new Properties();
                        mappings.load(is);
                        if (logger.isTraceEnabled()) {
                            logger.trace("Loaded schema mappings: " + mappings);
                        }
                        schemaMappings = new ConcurrentHashMap<>(mappings.size());
                        mergePropertiesIntoMap(mappings, schemaMappings);
                        this.schemaMappings = schemaMappings;
                    } catch (IOException ex) {
                        throw new IllegalStateException(
                                "Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
                    }
                }
            }
        }
        return schemaMappings;
    }

    private void mergePropertiesIntoMap(Properties props, Map<String, String> schemaMappings) {
        if (props != null) {
            Enumeration<?> en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = props.getProperty(key);
                schemaMappings.put(key, value);
            }
        }
    }
}
