package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.view.AbstractFXMLPane;
import com.oskopek.studyguide.view.StudyGuideApplication;

import javax.inject.Inject;
import java.util.ResourceBundle;

/**
 * Abstraction over controllers for {@link AbstractFXMLPane} successors.
 *
 * @param <T> the type of pane the controller controls
 */
public abstract class AbstractController<T extends AbstractFXMLPane> {

    protected T viewElement;

    protected StudyGuideApplication studyGuideApplication;

    @Inject
    protected ResourceBundle messages;

    /**
     * Set the {@link AbstractFXMLPane} successor to control.
     *
     * @param viewElement non-null
     */
    public void setViewElement(T viewElement) {
        this.viewElement = viewElement;
    }

    /**
     * Set the main app (and model) reference.
     *
     * @param studyGuideApplication non-null
     */
    public void setStudyGuideApplication(StudyGuideApplication studyGuideApplication) {
        this.studyGuideApplication = studyGuideApplication;
    }
}
