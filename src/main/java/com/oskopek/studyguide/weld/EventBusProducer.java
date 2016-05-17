package com.oskopek.studyguide.weld;

import com.google.common.eventbus.EventBus;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

public class EventBusProducer {

    @Produces
    @Singleton
    public EventBus createEventBus() {
        return new EventBus();
    }
}
