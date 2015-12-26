package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.view.SemesterBoxPane;
import com.oskopek.studyguide.view.SemesterPane;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

public class SemesterController extends AbstractController<SemesterPane> {

    @FXML
    private TilePane tilePane;

    @FXML
    private void initialize() {
        onAddSemester();
    }

    @FXML
    private void onAddSemester() {
        int semesterCount = tilePane.getChildren().size();
        int currentRows = semesterCount % 2 + semesterCount / 2;
        if (currentRows * 2 <= semesterCount) { // enough space
            tilePane.setPrefRows(currentRows+1);
        }
        tilePane.getChildren().add(new SemesterBoxPane(this.viewElement).load(studyGuideApplication));
    }

    public void removeSemester(SemesterBoxPane box) {
        int semesterCount = tilePane.getChildren().size() - 1;
        int currentRows = semesterCount % 2 + semesterCount / 2;
        if (currentRows * 2 - 2 >= semesterCount) { // more than enough space
            tilePane.setPrefRows(currentRows-1);
        }
        tilePane.getChildren().remove(box);
    }

    public void dragDetected(SemesterBoxPane box) { // TODO
        System.out.println("Drag detected in box " + box);
    }

    public void dragEnded(SemesterBoxPane box) { // TODO
        System.out.println("Drag ended in box " + box);
    }

}
