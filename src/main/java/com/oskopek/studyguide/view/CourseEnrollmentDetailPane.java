package com.oskopek.studyguide.view;

/**
 * Shows details of a selected {@link com.oskopek.studyguide.model.CourseEnrollment}.
 * Returns a GridPane.
 */
public class CourseEnrollmentDetailPane extends AbstractFXMLPane {

    @Override
    protected String getFxmlResource() {
        return "CourseEnrollmentDetailPane.fxml";
    }
}
