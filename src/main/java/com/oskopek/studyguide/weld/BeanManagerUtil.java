package com.oskopek.studyguide.weld;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class BeanManagerUtil {

    private BeanManagerUtil() {
        // intentionally empty
    }

    public static <T> T createBeanInstance(BeanManager beanManager, Class<T> clazz, final Annotation... qualifiers) {
        Bean<T> bean = (Bean<T>) beanManager.resolve(beanManager.getBeans(clazz, qualifiers));
        if (bean != null) {
            CreationalContext<T> creationalContext = beanManager.createCreationalContext(bean);
            if (creationalContext != null) {
                return bean.create(creationalContext);
            }
        } else {
            try { // TODO OPTIONAL HACK
                Constructor<T> constructor = clazz.getDeclaredConstructor(new Class[0]); // get the bean constructor
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                    InvocationTargetException e) {
                throw new IllegalArgumentException("BeanManager is null and failed to create instance.", e);
            }
        }
        return null;
    }

}
