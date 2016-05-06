package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.SemesterBoxController;
import com.oskopek.studyguide.model.Semester;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;

/**
 * Creates a new semester box (to be put into a Semester pane). Loads the view from the corresponding fxml file.
 */
@Singleton
public class SemesterBoxPaneCreator {

    @Inject
    @Named("fxmlloader")
    private Instance<FXMLLoader> fxmlLoader;

    /**
     * Create a new BorderPane element from {@code SemesterBoxPane.fxml}. Opens a UI alert pop-up on error.
     *
     * @param semester the semester for which we're creating the box
     * @return an observable BorderPane, ready to be put into a Semester pane
     * @see com.oskopek.studyguide.controller.SemesterController#semesterBoxes
     */
    public SemesterBoxController create(Semester semester) {
        FXMLLoader fxmlLoader = this.fxmlLoader.get();
        BorderPane semesterBoxPane = null;
        try (InputStream is = getClass().getResourceAsStream("SemesterBoxPane.fxml")) {
            semesterBoxPane = fxmlLoader.load(is);
        } catch (IOException e) {
            AlertCreator.handleLoadLayoutError(fxmlLoader.getResources(), e);
        }
        SemesterBoxController semesterBoxController = fxmlLoader.getController();
        semesterBoxController.setSemester(semester);
        semesterBoxController.setPane(semesterBoxPane);
        return semesterBoxController;
    }
}
