package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;
import com.oskopek.studyguide.persistence.MFFWebScraperUtil;
import com.oskopek.studyguide.persistence.SISHtmlScraper;
import com.oskopek.studyguide.view.AlertCreator;
import com.oskopek.studyguide.view.EnterStringDialogPaneCreator;
import com.oskopek.studyguide.view.ProgressCreator;
import com.oskopek.studyguide.view.SemesterBoxPaneCreator;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    @Inject
    private EnterStringDialogPaneCreator enterStringDialogPaneCreator;

    private ChangeListener<List<Semester>> listChangeListener;

    /**
     * Initializes the {@link #semesterBoxes} data bindings.
     */
    @FXML
    private void initialize() {
        listChangeListener = (observable, oldValue, newValue) -> reinitializeSemesterBoxes();
        studyGuideApplication.studyPlanProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.getSemesterPlan().semesterListProperty().removeListener(listChangeListener);
            }
            newValue.getSemesterPlan().semesterListProperty().addListener(listChangeListener);
            reinitializeSemesterBoxes();
        }));

        // add column constraints for both columns
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        semesterBoxes.getColumnConstraints().add(columnConstraints);
        semesterBoxes.getColumnConstraints().add(columnConstraints);
    }

    /**
     * Clears the semester boxes and creates new one upon a change.
     */
    private void reinitializeSemesterBoxes() {
        semesterBoxes.getChildren().clear();
        int i = 0;
        for (Semester semester : studyGuideApplication.getStudyPlan().getSemesterPlan()) {
            SemesterBoxController semesterBoxController = semesterBoxPaneCreator.create(semester);
            logger.debug("Creating semester box controller {}", semesterBoxController);
            BorderPane semesterBox = semesterBoxController.getPane();
            semesterBoxes.add(semesterBox, i % 2, i / 2);
            i++;
        }
    }

    /**
     * Handles adding a new semester to the pane and model.
     */
    @FXML
    private void handleAddSemester() {
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
    private void handleNewCourse() {
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
     * Handles creating adding a new course from a network resource based on it's ID.
     *
     * @see SISHtmlScraper#scrapeCourse(com.oskopek.studyguide.model.courses.CourseRegistry, String)
     */
    @FXML
    private void handleAddCourseFrom() {
        StudyPlan studyPlan = studyGuideApplication.getStudyPlan();
        if (studyPlan == null) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, messages.getString("course.cannotAdd"));
            return;
        }
        EnterStringController enterStringController = enterStringDialogPaneCreator.create(
                messages.getString("semester.enterCourseId"));
        Optional<ButtonType> result = enterStringController.getDialog().showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            String submittedCourseId = enterStringController.getSubmittedString();
            SISHtmlScraper scraper = new SISHtmlScraper(MFFWebScraperUtil.sisWebUrl);
            Stage progressDialog = ProgressCreator.showProgress(scraper, messages.getString("progress.pleaseWait"));
            Task<Course> courseTask = new Task<Course>() {
                @Override
                protected Course call() throws Exception {
                    return scraper.scrapeCourse(studyPlan.getCourseRegistry(), submittedCourseId);
                }
            };
            courseTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
                progressDialog.close();
                AlertCreator.showAlert(Alert.AlertType.ERROR,
                        messages.getString("semesterPane.addCourseFromFailed") + ":\n\n"
                                + newValue.getLocalizedMessage());
                throw new IllegalStateException("Scrape courses from failed.", newValue);
            });
            courseTask.setOnSucceeded(event -> {
                progressDialog.close();
                studyPlan.getCourseRegistry().putCourse(courseTask.getValue());
            });
            courseTask.setOnFailed(event -> {
                progressDialog.close();
                AlertCreator.showAlert(Alert.AlertType.ERROR,
                        messages.getString("semesterPane.addCourseFromFailed"));
            });
            new Thread(courseTask).start();
        }
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
     * Checks and moves a course enrollment from one semester to the other. Used for drag and drop operations.
     *
     * @param from the source semester
     * @param to the target semester
     * @param enrollment the enrollment to move from the {@code from} semester to the {@code to} semester
     */
    public void moveCourseEnrollment(Semester from, Semester to, CourseEnrollment enrollment) {
        if (from == null || to == null || enrollment == null || enrollment.getSemester() == null
                || !from.getCourseEnrollmentList().contains(enrollment) || !enrollment.getSemester().equals(from)) {
            throw new IllegalArgumentException("The course enrollment is not correctly set in the from semester.");
        }
        // we do not change course enrollment constraints when moving
        try {
            to.addCourseEnrollment(enrollment);
        } catch (IllegalArgumentException e) {
            logger.info("Moving Course enrollment ({}) to wrong semester({}), showing error box. (enrolled twice)",
                    enrollment, to);
            AlertCreator.showAlert(Alert.AlertType.ERROR,
                    messages.getString("findCourses.courseAlreadyEnrolled"));
            return;
        }
        from.removeCourseEnrollment(enrollment);
        enrollment.semesterProperty().set(to);
    }

}
