package com.oskopek.studyguide.model;

import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.weld.EventBusTranslator;

public interface Registrable<T> {

    T register(EventBus eventBus, EventBusTranslator eventBusTranslator);

    T unregister(EventBus eventBus, EventBusTranslator eventBusTranslator);
}
