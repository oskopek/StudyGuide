package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;
import com.oskopek.studyguide.view.AlertCreator;
import com.oskopek.studyguide.view.SemesterBoxPaneCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Controller for SemesterPane.
 * Handles adding/removing {@link com.oskopek.studyguide.model.Semester}s
 * and dragging {@link com.oskopek.studyguide.model.courses.Course}s between them.
 */
@Singleton
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

    @Inject
    private CourseDetailController courseDetailController;

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
        StudyPlan studyPlan = studyGuideApplication.getStudyPlan();
        if (studyPlan == null) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, "Cannot create a course when there is no study plan loaded!");
            return;
        }
        studyPlan.getSemesterPlan().addSemester(new Semester("Semester" + id++));
    }

    /**
     * Handles creating a new course and adding it to the latest semester for editing.
     */
    @FXML
    private void onNewCourse() {
        StudyPlan studyPlan = studyGuideApplication.getStudyPlan();
        if (studyPlan == null) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, "Cannot create a course when there is no study plan loaded!");
            return;
        }
        int courseId = 0;
        while (studyPlan.getCourseRegistry().getCourse("Course" + courseId) != null) {
            courseId++;
        }
        Course course = new Course("Course" + courseId, "Name", "LocalizedName", Locale.getDefault(), Credits.valueOf(0),
                new ArrayList<>(Arrays.asList("teacher")), new ArrayList<>(), new ArrayList<>());
        studyPlan.getCourseRegistry().putCourse(course);
        courseDetailController.setCourse(course);
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
