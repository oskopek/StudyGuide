package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.view.SemesterBoxPane;
import com.oskopek.studyguide.view.SemesterPane;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

import java.util.HashMap;

public class SemesterController extends AbstractController<SemesterPane> {

    @FXML
    private TilePane tilePane;

    /**
     * Needs to be done after loading the graphics.
     */
    public void initializeSemesters() {
        onAddSemester();
    }

    @FXML
    private void onAddSemester() {
        int semesterCount = tilePane.getChildren().size();
        int currentRows = semesterCount % 2 + semesterCount / 2;
        if (currentRows * 2 <= semesterCount) { // enough space
            tilePane.setPrefRows(currentRows+1);
        }
        SemesterBoxPane boxPane = new SemesterBoxPane(this.viewElement);
        BorderPane borderPane = (BorderPane) boxPane.load(studyGuideApplication);
        boxPane.setBoxBorderPane(borderPane);
        tilePane.getChildren().add(borderPane);
    }

    public void removeSemester(SemesterBoxPane box) {
        BorderPane borderPane = box.getBoxBorderPane();
        if (borderPane == null) {
            throw new IllegalStateException("No such semester box found!");
        }
        tilePane.getChildren().remove(borderPane);

        int semesterCount = tilePane.getChildren().size() - 1;
        int currentRows = semesterCount % 2 + semesterCount / 2;
        if (currentRows * 2 - 2 >= semesterCount) { // more than enough space
            tilePane.setPrefRows(currentRows-1);
        }
    }

    public void dragDetected(SemesterBoxPane box) { // TODO
        System.out.println("Drag detected in box " + box);
    }

    public void dragEnded(SemesterBoxPane box) { // TODO
        System.out.println("Drag ended in box " + box);
    }

}
