package com.oskopek.studyguide.weld;

import com.google.common.eventbus.EventBus;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * A singleton Guava {@link EventBus} producer.
 * Used primarily for constraint events.
 */
public final class EventBusProducer {

    /**
     * Produce a (singleton) Guava {@link EventBus}. Used for constraint events and model changes.
     *
     * @return the created event bus
     */
    @Produces
    @Singleton
    public EventBus createEventBus() {
        return new EventBus();
    }
}
