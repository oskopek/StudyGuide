package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.view.SemesterBoxPane;
import com.oskopek.studyguide.view.SemesterPane;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for {@link SemesterPane}.
 * Handles adding/removing {@link com.oskopek.studyguide.model.Semester}s
 * and dragging {@link com.oskopek.studyguide.model.courses.Course}s between them.
 */
public class SemesterController extends AbstractController<SemesterPane> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TilePane tilePane;

    /**
     * Adds a default, initial semester. Needs to be done after loading the graphics.
     */
    public void initializeSemesters() {
        onAddSemester();
    } // TODO add the add semester button to menu

    /**
     * Handles adding a new semester to the pane and model.
     */
    @FXML
    private void onAddSemester() {
        SemesterBoxPane boxPane = new SemesterBoxPane(this.viewElement);
        BorderPane borderPane = (BorderPane) boxPane.load(studyGuideApplication);
        boxPane.setBoxBorderPane(borderPane);
        tilePane.getChildren().add(borderPane);
    }

    /**
     * Removes a semester from the semester pane.
     *
     * @param box non-null, with a non null {@link SemesterBoxPane#getBoxBorderPane()}
     */
    public void removeSemester(SemesterBoxPane box) {
        BorderPane borderPane = box.getBoxBorderPane();
        if (borderPane == null) {
            throw new IllegalStateException("No such semester box found!");
        }
        tilePane.getChildren().remove(borderPane);
    }

    /**
     * Handles the start of a {@link com.oskopek.studyguide.model.courses.Course}
     * drag and drop action between semesters.
     *
     * @param box non-null
     */
    public void dragDetected(SemesterBoxPane box) { // TODO drag n drop
        logger.debug("Drag detected in box {}", box);
    }

    /**
     * Handles the end of a {@link com.oskopek.studyguide.model.courses.Course} drag and drop action between semesters.
     *
     * @param box non-null
     */
    public void dragEnded(SemesterBoxPane box) { // TODO drag n drop
        logger.debug("Drag ended in box {}", box);
    }

}
