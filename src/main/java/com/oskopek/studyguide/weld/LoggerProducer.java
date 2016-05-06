package com.oskopek.studyguide.weld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * A simple {@link Logger} producer using a {@link LoggerFactory} and reflection.
 */
public class LoggerProducer {

    /**
     * Create a logger for the class where we are injecting it in using reflection.
     *
     * @param injectionPoint the logger injection point
     * @return an initialized logger specialized for the injecting class
     */
    @Produces
    public Logger createLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

}
