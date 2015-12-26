package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.view.AbstractFXMLPane;
import com.oskopek.studyguide.view.StudyGuideApplication;

public abstract class AbstractController<T extends AbstractFXMLPane> {

    protected T viewElement;

    protected StudyGuideApplication studyGuideApplication;

    public void setViewElement(T viewElement) {
        this.viewElement = viewElement;
    }

    public void setStudyGuideApplication(StudyGuideApplication studyGuideApplication) {
        this.studyGuideApplication = studyGuideApplication;
    }
}
