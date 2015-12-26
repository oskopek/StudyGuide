package com.oskopek.studyguide.view;

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
    public Node load() {
        VBox studyPane = (VBox) super.load();

        VBox findCoursePane = (VBox) new FindCoursePane().load();
        studyPane.getChildren().add(findCoursePane);

        Separator separator = new Separator();
        separator.setPrefWidth(200);
        studyPane.getChildren().add(separator);

        GridPane courseEnrollmentDetailPane = (GridPane) new CourseEnrollmentDetailPane().load();
        studyPane.getChildren().add(courseEnrollmentDetailPane);

        return studyPane;
    }

    @Override
    protected String getFxmlResource() {
        return "StudyPane.fxml";
    }
}
