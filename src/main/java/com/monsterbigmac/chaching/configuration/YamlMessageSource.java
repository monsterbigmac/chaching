package com.monsterbigmac.chaching.configuration;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public class YamlMessageSource extends AbstractResourceBasedMessageSource {

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = getMessageFromYaml(code, locale);
        if (msg == null) {
            return null;
        }
        return new MessageFormat(msg, locale);
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        return getMessageFromYaml(code, locale);
    }

    private String getMessageFromYaml(String code, Locale locale) {
        Properties properties = loadYamlProperties(locale);
        if (properties == null) {
            return null;
        }
        return properties.getProperty(code);
    }

    private Properties loadYamlProperties(Locale locale) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String filename = getBasenameSet().iterator().next() + "_" + locale.toString() + ".yml";

        try {
            Resource resource = resolver.getResource("classpath:" + filename);
            if (!resource.exists()) {
                // Fallback to default locale
                filename = getBasenameSet().iterator().next() + "_en.yml";
                resource = resolver.getResource("classpath:" + filename);
            }

            if (resource.exists()) {
                YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
                factory.setResources(resource);
                return flattenProperties("", Objects.requireNonNull(factory.getObject()));
            }
        } catch (Exception e) {
            logger.error("Error loading YAML message file: " + filename, e);
        }

        return null;
    }

    private Properties flattenProperties(String prefix, Properties properties) {
        Properties flatProperties = new Properties();
        properties.forEach((key, value) -> {
            String fullKey = prefix.isEmpty() ? key.toString() : prefix + "." + key;
            if (value instanceof Properties) {
                flatProperties.putAll(flattenProperties(fullKey, (Properties) value));
            } else {
                flatProperties.setProperty(fullKey, value.toString());
            }
        });
        return flatProperties;
    }
}
