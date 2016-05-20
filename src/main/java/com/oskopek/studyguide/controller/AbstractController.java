package com.oskopek.studyguide.controller;

import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.view.StudyGuideApplication;

import javax.inject.Inject;
import java.util.ResourceBundle;

/**
 * Abstraction over controllers.
 */
public abstract class AbstractController {

    @Inject
    protected StudyGuideApplication studyGuideApplication;

    @Inject
    protected transient ResourceBundle messages;

    @Inject
    protected EventBus eventBus;

}
