package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.FindCoursesController;
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

    private FindCoursesController findCoursesController;

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        VBox studyPane = (VBox) super.load(studyGuideApplication);

        FindCoursePane findCoursePane = new FindCoursePane();
        VBox findCoursePaneBox = (VBox) findCoursePane.load(studyGuideApplication);
        studyPane.getChildren().add(findCoursePaneBox);
        findCoursesController = (FindCoursesController) findCoursePane.getController();

        Separator separator = new Separator();
        separator.setPrefWidth(200);
        studyPane.getChildren().add(separator);

        GridPane courseEnrollmentDetailPane = (GridPane) new CourseEnrollmentDetailPane().load(studyGuideApplication);
        studyPane.getChildren().add(courseEnrollmentDetailPane);

        StudyController controller = (StudyController) getController();
        controller.setViewElement(this);

        return studyPane;
    }

    /**
     * The {@link FindCoursesController} for the course search panel.
     * @return non-null
     */
    public FindCoursesController getFindCoursesController() {
        return findCoursesController;
    }

    @Override
    protected String getFxmlResource() {
        return "StudyPane.fxml";
    }
}
