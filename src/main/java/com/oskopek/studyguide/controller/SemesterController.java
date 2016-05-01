package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.view.SemesterBoxPaneCreator;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;

/**
 * Controller for SemesterPane.
 * Handles adding/removing {@link com.oskopek.studyguide.model.Semester}s
 * and dragging {@link com.oskopek.studyguide.model.courses.Course}s between them.
 */
public class SemesterController extends AbstractController {

    private int id = 0;

    @FXML
    private TableView<Semester> semesterBoxTable;

    @FXML
    private TableColumn<Semester, BorderPane> winterSemesterColumn;

    @FXML
    private TableColumn<Semester, BorderPane> summerSemesterColumn;

    @Inject
    private SemesterBoxPaneCreator semesterBoxPaneCreator;

    /**
     * Initializes the {@link #semesterBoxTable} data bindings.
     */
    @FXML
    private void initialize() {
        winterSemesterColumn.setCellValueFactory(cellData -> semesterBoxPaneCreator.create(cellData.getValue()));
        summerSemesterColumn.setCellValueFactory(cellData -> semesterBoxPaneCreator.create(cellData.getValue()));
        semesterBoxTable.itemsProperty().bindBidirectional(
                studyGuideApplication.getStudyPlan().getSemesterPlan().semesterListProperty());
    }

    /**
     * Handles adding a new semester to the pane and model.
     */
    @FXML
    private void onAddSemester() {
        studyGuideApplication.getStudyPlan().getSemesterPlan().addSemester(new Semester("Semester" + id++));
    }

    /**
     * Removes a semester from the semester pane.
     *
     * @param semester the semester to remove
     */
    public void removeSemester(Semester semester) {
        studyGuideApplication.getStudyPlan().getSemesterPlan().removeSemester(semester);
    }

    /**
     * Handler of detected drag in the table.
     */
    @FXML
    public void onDragDetected() {
        logger.debug("Drag detected {}");
    }

    /**
     * Handler of dropped drag in the table.
     *
     * @param e the drag event
     */
    @FXML
    public void onDragDropped(DragEvent e) {
        logger.debug("Drag dropped {}");
    }

    /**
     * Handler of finished drag in the table.
     *
     * @param e the drag event
     */
    @FXML
    public void onDragDone(DragEvent e) {
        logger.debug("Drag done {}");
    }

}
