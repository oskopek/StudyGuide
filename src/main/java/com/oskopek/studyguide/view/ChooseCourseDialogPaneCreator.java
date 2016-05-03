package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.ChooseCourseController;
import com.oskopek.studyguide.model.courses.Course;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Dialog for choosing found courses. Returns a DialogPane.
 *
 * @see ChooseCourseController
 */
@Singleton
public class ChooseCourseDialogPaneCreator {

    @Inject
    private FXMLLoader fxmlLoader;

    /**
     * Create the dialog for choosing courses.
     *
     * @param courseList the list of courses to show in the dialog (let the user pick from them)
     * @param dialog the dialog instance
     * @return the controller of the dialog window, enabling to display the dialog and read the selected result
     */
    public ChooseCourseController create(List<Course> courseList, Dialog<ButtonType> dialog) {
        try (InputStream is = getClass().getResourceAsStream("ChooseCourseDialogPane.fxml")) {
            fxmlLoader.load(is);
        } catch (IOException e) {
            AlertCreator.handleLoadLayoutError(fxmlLoader.getResources(), e);
        }
        ChooseCourseController chooseCourseController = fxmlLoader.getController();
        chooseCourseController.setCourseList(courseList);
        chooseCourseController.setDialog(dialog);
        return chooseCourseController;
    }
}
