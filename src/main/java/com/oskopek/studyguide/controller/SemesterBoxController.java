package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.view.AbstractFXMLPane;
import com.oskopek.studyguide.view.SemesterBoxPane;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class SemesterBoxController extends AbstractController<SemesterBoxPane> {

    private static int index = 0;

    @FXML
    private TextField semesterNameArea;

    private Semester semester;

    public void initializeSemester() {
        semester = new Semester("Semester" + index++);
        studyGuideApplication.getStudyPlan().getSemesterPlan().getSemesterList().add(semester);
        semesterNameArea.setText(semester.getName());
    }

    private SemesterController getParentController() {
        return (SemesterController) viewElement.getParent().getController();
    }

    @FXML
    public void onRemoveSemester() {
        studyGuideApplication.getStudyPlan().getSemesterPlan().getSemesterList().remove(semester);
        getParentController().removeSemester(this.viewElement);
    }

    @FXML
    public void onSemesterNameChange() {
        String newName = semesterNameArea.getText();
        if (studyGuideApplication.getStudyPlan().getSemesterPlan().getSemesterList().contains(new Semester(newName))) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    AbstractFXMLPane.messages.getString("semesterBox.nameNotUnique"));
            alert.showAndWait();
        } else {
            semester.setName(newName);
        }
    }

    @FXML
    public void onDragDetected() {
        getParentController().dragDetected(this.viewElement);
    }

    @FXML
    public void onDragDropped() {
        getParentController().dragEnded(this.viewElement);
    }

}
