package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.SemesterController;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * List of courses and credits and isFulfilled (replicates {@link com.oskopek.studyguide.model.SemesterPlan}).
 * Returns a BorderPane.
 */
public class SemesterPane extends AbstractFXMLPane {

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        BorderPane findCoursePane = (BorderPane) super.load(studyGuideApplication);
        SemesterController controller = (SemesterController) getController();
        controller.setViewElement(this);
        controller.setStudyGuideApplication(studyGuideApplication);
        return findCoursePane;
    }

    @Override
    protected String getFxmlResource() {
        return "SemesterPane.fxml";
    }
}
