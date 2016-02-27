package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.ChooseCourseController;
import com.oskopek.studyguide.model.courses.Course;
import javafx.scene.Node;
import javafx.scene.control.DialogPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog for choosing found courses. Returns a DialogPane.
 *
 * @see ChooseCourseController
 */
public class ChooseCourseDialogPane extends AbstractFXMLPane {

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        return load(studyGuideApplication, new ArrayList<>());
    }

    /**
     * {@inheritDoc}
     * @param courseList the list of {@link Course}s to display
     */
    public Node load(StudyGuideApplication studyGuideApplication, List<Course> courseList) {
        DialogPane chooseCourseDialogPane = (DialogPane) super.load(studyGuideApplication);
        ChooseCourseController chooseCourseController = (ChooseCourseController) getController();
        chooseCourseController.setViewElement(this);
        chooseCourseController.setStudyGuideApplication(studyGuideApplication);
        chooseCourseController.setCourseList(courseList);
        return chooseCourseDialogPane;
    }

    @Override
    protected String getFxmlResource() {
        return "ChooseCourseDialogPane.fxml";
    }
}
