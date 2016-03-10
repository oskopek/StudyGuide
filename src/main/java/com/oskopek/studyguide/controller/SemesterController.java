package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.view.SemesterBoxPane;
import com.oskopek.studyguide.view.SemesterPane;
import com.oskopek.studyguide.view.StudyGuideApplication;
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
     * Clear the existing {@link TilePane} and build new {@link SemesterBoxPane}s for all semester in the model.
     *
     * @see StudyGuideApplication#getStudyPlan()
     * @see SemesterPane#load(StudyGuideApplication)
     */
    public void reinitialize() {
        tilePane.getChildren().clear();
        studyGuideApplication.getStudyPlan().getSemesterPlan().getSemesterList().stream().forEach(this::addSemester);
    }

    /**
     * Handles adding a new semester to the pane and model.
     */
    @FXML
    private void onAddSemester() {  // TODO add the add semester button to menu (make #addSemester public)
        addSemester(null);
    }

    /**
     * Add a new {@link SemesterBoxPane} with the given {@link Semester}.
     * Does not add the semester to the model (except {@code semester == null}).
     *
     * @param semester if null, creates a new Semester and adds it to the model
     */
    private void addSemester(Semester semester) {
        SemesterBoxPane boxPane = new SemesterBoxPane(this.viewElement);
        BorderPane borderPane = (BorderPane) boxPane.load(studyGuideApplication);
        SemesterBoxController controller = (SemesterBoxController) boxPane.getController();
        if (semester != null) {
            controller.setSemester(semester);
        } else {
            controller.initializeEmptySemester();
        }
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
