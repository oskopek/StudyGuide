package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.ChooseCourseController;
import com.oskopek.studyguide.controller.FindCoursesController;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Panel for finding courses. Returns a VBox.
 *
 * @see FindCoursesController
 */
public class FindCoursePane extends AbstractFXMLPane {

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        VBox findCoursePane = (VBox) super.load(studyGuideApplication);
        FindCoursesController findCoursesController = (FindCoursesController) getController();
        findCoursesController.setViewElement(this);
        findCoursesController.setStudyGuideApplication(studyGuideApplication);
        return findCoursePane;
    }

    @Override
    protected String getFxmlResource() {
        return "FindCoursePane.fxml";
    }
}
