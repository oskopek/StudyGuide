package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.CourseEnrollmentDetailController;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * Shows details of a selected {@link com.oskopek.studyguide.model.CourseEnrollment}.
 * Returns a GridPane.
 */
public class CourseEnrollmentDetailPane extends AbstractFXMLPane {

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        GridPane courseEnrollmentDetailPane = (GridPane) super.load(studyGuideApplication);
        CourseEnrollmentDetailController courseEnrollmentDetailController
                = (CourseEnrollmentDetailController) getController();
        courseEnrollmentDetailController.setViewElement(this);
        courseEnrollmentDetailController.setStudyGuideApplication(studyGuideApplication);
        return courseEnrollmentDetailPane;
    }

    @Override
    protected String getFxmlResource() {
        return "CourseEnrollmentDetailPane.fxml";
    }
}
