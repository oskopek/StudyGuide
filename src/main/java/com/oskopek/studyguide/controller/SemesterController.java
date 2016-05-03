package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;
import com.oskopek.studyguide.view.AlertCreator;
import com.oskopek.studyguide.view.SemesterBoxPaneCreator;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Controller for SemesterPane.
 * Handles adding/removing {@link com.oskopek.studyguide.model.Semester}s
 * and dragging {@link com.oskopek.studyguide.model.courses.Course}s between them.
 */
public class SemesterController extends AbstractController {

    private int id = 0;

    @FXML
    private GridPane semesterBoxes;

    @Inject
    private SemesterBoxPaneCreator semesterBoxPaneCreator;

    @Inject
    private CourseDetailController courseDetailController;

    private ChangeListener<List<Semester>> listChangeListener;

    /**
     * Initializes the {@link #semesterBoxes} data bindings.
     */
    @FXML
    private void initialize() { // TODO do we have all the binds in a wrong way?
        listChangeListener = (observable, oldValue, newValue) -> reinitializeSemesterBoxes();
        studyGuideApplication.studyPlanProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.getSemesterPlan().semesterListProperty().removeListener(listChangeListener);
            }
            newValue.getSemesterPlan().semesterListProperty().addListener(listChangeListener);
            reinitializeSemesterBoxes();
        }));
    }

    private void reinitializeSemesterBoxes() {
        semesterBoxes.getChildren().clear();
        int i = 0;
        for (Semester semester : studyGuideApplication.getStudyPlan().getSemesterPlan()) {
            BorderPane semesterBox = semesterBoxPaneCreator.create(semester);
            semesterBoxes.add(semesterBox, i % 2, i / 2);
            i++;
        }
    }

    /**
     * Handles adding a new semester to the pane and model.
     */
    @FXML
    private void onAddSemester() {
        StudyPlan studyPlan = studyGuideApplication.getStudyPlan();
        if (studyPlan == null) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, messages.getString("semester.cannotAdd"));
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
            AlertCreator.showAlert(Alert.AlertType.ERROR, messages.getString("course.cannotAdd"));
            return;
        }
        int courseId = 0;
        while (studyPlan.getCourseRegistry().getCourse("Course" + courseId) != null) {
            courseId++;
        }
        Course course = new Course("Course" + courseId, "Name", "LocalizedName", Locale.getDefault(),
                Credits.valueOf(0), new ArrayList<>(Arrays.asList("teacher")), new ArrayList<>(), new ArrayList<>());
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
