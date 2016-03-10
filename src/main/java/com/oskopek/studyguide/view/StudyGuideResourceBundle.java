package com.oskopek.studyguide.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Resource bundle implemented as a wrapper around {@link PropertyResourceBundle} with base name {@code sg_messages}.
 */
public class StudyGuideResourceBundle extends ResourceBundle {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String baseName = "sg_messages";
    private ResourceBundle bundle;

    @Override
    protected Object handleGetObject(String key) {
        return bundle.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return bundle.getKeys();
    }

    /**
     * Gets a {@link PropertyResourceBundle} from {@code baseName_localeLanguage.properties}.
     *
     * @param baseName the base name of the bundle
     * @param locale   the locale from which to get the language
     */
    private StudyGuideResourceBundle(String baseName, Locale locale) {
        String resourceString = baseName + "_" + locale.getLanguage() + ".properties";
        try {
            bundle = new PropertyResourceBundle(getClass().getResourceAsStream(resourceString));
        } catch (IOException e) {
            logger.error("Failed to load resource bundle from {}", resourceString);
            bundle = ResourceBundle.getBundle(baseName, locale);
        }
    }

    /**
     * Gets a {@link PropertyResourceBundle} from {@code baseName_localeLanguage.properties}.
     * Use the default {@code baseName}.
     *
     * @param locale the locale from which to get the language
     */
    public StudyGuideResourceBundle(Locale locale) {
        this(baseName, locale);
    }

    /**
     * Gets a {@link PropertyResourceBundle} from {@code baseName_localeLanguage.properties}.
     * Use the default {@code baseName} and {@link Locale}.
     */
    public StudyGuideResourceBundle() {
        this(Locale.getDefault());
    }

}
