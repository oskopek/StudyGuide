package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.ChooseCourseController;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.weld.FXMLLoaderProducer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Dialog for choosing found courses. Returns a DialogPane.
 *
 * @see ChooseCourseController
 */
public class ChooseCourseDialogPaneCreator {

    @Inject
    private FXMLLoader fxmlLoader;

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
