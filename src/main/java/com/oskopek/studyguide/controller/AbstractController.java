package com.oskopek.studyguide.controller;

import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.view.StudyGuideApplication;
import com.oskopek.studyguide.weld.EventBusTranslator;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ResourceBundle;

/**
 * Abstraction over controllers.
 */
public abstract class AbstractController {

    @Inject
    protected StudyGuideApplication studyGuideApplication;

    @Inject
    protected transient Logger logger;

    @Inject
    protected transient ResourceBundle messages;

    @Inject
    protected EventBusTranslator eventBusTranslator;

    @Inject
    protected EventBus eventBus;

}
