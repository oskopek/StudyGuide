package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.ChooseCourseController;
import com.oskopek.studyguide.model.courses.Course;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.util.List;

/**
 * Dialog for choosing found courses. Returns a DialogPane.
 *
 * @see ChooseCourseController
 */
public class ChooseCourseDialogPane extends AbstractFXMLPane {

    /**
     * {@inheritDoc}
     * Should not be called on instances of {@link ChooseCourseDialogPane},
     * will fail with an {@link NullPointerException} on double click in table (on purpose).
     */
    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        throw new UnsupportedOperationException("Call the other load method in ChooseCourseDialogPane!");
    }

    /**
     * {@inheritDoc}
     *
     * @param courseList the list of {@link Course}s to display
     */
    public Node load(StudyGuideApplication studyGuideApplication, List<Course> courseList, Dialog<ButtonType> dialog) {
        DialogPane chooseCourseDialogPane = (DialogPane) super.load(studyGuideApplication);
        ChooseCourseController chooseCourseController = (ChooseCourseController) getController();
        chooseCourseController.setViewElement(this);
        chooseCourseController.setStudyGuideApplication(studyGuideApplication);
        chooseCourseController.setCourseList(courseList);
        chooseCourseController.setDialog(dialog);
        return chooseCourseDialogPane;
    }

    @Override
    protected String getFxmlResource() {
        return "ChooseCourseDialogPane.fxml";
    }
}
