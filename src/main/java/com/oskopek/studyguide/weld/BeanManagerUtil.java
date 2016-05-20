package com.oskopek.studyguide.weld;

import javax.enterprise.inject.spi.CDI;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class BeanManagerUtil {

    private BeanManagerUtil() {
        // intentionally empty
    }

    public static <T> T createBeanInstance(Class<T> clazz) {
        try {
            return CDI.current().select(clazz).get();
        } catch (IllegalStateException ise) { // CDI disabled
            try { // TODO OPTIONAL HACK
                Constructor<T> constructor = clazz.getDeclaredConstructor(); // get the bean constructor
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                    | InvocationTargetException e) {
                throw new IllegalArgumentException("Failed to create non-CDI instance.", e);
            }
        }
    }

}
