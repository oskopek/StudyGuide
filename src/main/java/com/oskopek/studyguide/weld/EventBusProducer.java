package com.oskopek.studyguide.weld;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * A singleton Guava {@link EventBus} producer.
 * Used primarily for constraint events.
 */
public final class EventBusProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Produce a (singleton) Guava {@link EventBus}. Used for constraint events and model changes.
     *
     * @return the created event bus
     */
    @Produces
    @Singleton
    public EventBus createEventBus() {
        return new EventBus((throwable, subscriberExceptionContext) -> {
            logger.error("Exception ({}) occurred in an event subscriber ({}) for the event {}", throwable,
                    subscriberExceptionContext.getSubscriberMethod(), subscriberExceptionContext.getEvent());
        });
    }
}
