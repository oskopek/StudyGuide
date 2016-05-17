package com.oskopek.studyguide.weld;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Listener for undelivered events. Logs the event as an error.
 */
@Singleton
public final class DeadEventListener {

    @Inject
    private transient Logger logger;

    /**
     * Private default constructor for CDI and to prevent instantiation.
     */
    private DeadEventListener() {
        // intentionally empty
    }

    /**
     * Listen to dead events and log them.
     *
     * @param event the dead event
     */
    @Subscribe
    private void listen(DeadEvent event) {
        logger.error("Dead event ({}) pushed from ({})", event.getEvent(), event.getSource());
    }
}
