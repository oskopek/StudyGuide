package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.StudyController;
import com.oskopek.studyguide.model.StudyPlan;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Semester bubbles, drag n drop (replicates {@link StudyPlan}).
 * Returns a VBox.
 */
public class StudyPane extends AbstractFXMLPane {

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        VBox studyPane = (VBox) super.load(studyGuideApplication);

        VBox findCoursePane = (VBox) new FindCoursePane().load(studyGuideApplication);
        studyPane.getChildren().add(findCoursePane);

        Separator separator = new Separator();
        separator.setPrefWidth(200);
        studyPane.getChildren().add(separator);

        GridPane courseEnrollmentDetailPane = (GridPane) new CourseEnrollmentDetailPane().load(studyGuideApplication);
        studyPane.getChildren().add(courseEnrollmentDetailPane);

        StudyController controller = (StudyController) getController();
        controller.setViewElement(this);

        return studyPane;
    }

    @Override
    protected String getFxmlResource() {
        return "StudyPane.fxml";
    }
}
