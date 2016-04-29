package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.SemesterBoxController;
import com.oskopek.studyguide.model.Semester;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

public class SemesterBoxPaneCreator {

    @Inject
    private FXMLLoader fxmlLoader;

    public ObservableValue<BorderPane> create(Semester semester) {
        BorderPane semesterBoxPane = null;
        try (InputStream is = getClass().getResourceAsStream("SemesterBoxPane.fxml")) {
            semesterBoxPane = fxmlLoader.load(is);
        } catch (IOException e) {
            AlertCreator.handleLoadLayoutError(fxmlLoader.getResources(), e);
        }
        SemesterBoxController semesterBoxController = fxmlLoader.getController();
        semesterBoxController.setSemester(semester);
        return new ReadOnlyObjectWrapper<>(semesterBoxPane);
    }
}