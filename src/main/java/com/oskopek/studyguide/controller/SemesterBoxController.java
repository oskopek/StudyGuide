package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.view.AbstractFXMLPane;
import com.oskopek.studyguide.view.SemesterBoxPane;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for {@link SemesterBoxPane}.
 * Represents a single {@link Semester} in a {@link com.oskopek.studyguide.model.SemesterPlan}
 *
 * @see com.oskopek.studyguide.view.SemesterPane
 */
public class SemesterBoxController extends AbstractController<SemesterBoxPane> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static int index = 0;

    @FXML
    private TextField semesterNameArea;

    private Semester semester;

    @FXML
    private void initialize() {
        semesterNameArea.textProperty().addListener((observable) -> onSemesterNameChange());
    }

    /**
     * Initialize a new {@link Semester} instance into this box.
     * Needed, because JavaFX's initialize method runs too soon (before we have a reference to the main app).
     *
     * @see AbstractController#setStudyGuideApplication(com.oskopek.studyguide.view.StudyGuideApplication)
     * @see SemesterBoxPane#load(com.oskopek.studyguide.view.StudyGuideApplication)
     */
    public void initializeSemester() {
        semester = new Semester("Semester" + index++);
        studyGuideApplication.getStudyPlan().getSemesterPlan().addSemester(semester);
        semesterNameArea.setText(semester.getName());
    }

    /**
     * Get the parent controller. Used for inter-semester data exchange.
     *
     * @return non-null controller for {@link com.oskopek.studyguide.view.SemesterPane}
     * @see #onRemoveSemester()
     * @see #onDragDetected()
     * @see #onDragDropped()
     */
    private SemesterController getParentController() {
        return (SemesterController) viewElement.getParent().getController();
    }

    /**
     * Handles removing this whole box. Calls {@link SemesterController#removeSemester(SemesterBoxPane)}.
     */
    @FXML
    public void onRemoveSemester() {
        studyGuideApplication.getStudyPlan().getSemesterPlan().removeSemester(semester);
        getParentController().removeSemester(this.viewElement);
    }

    /**
     * Handles changing the name of this semester box. The name has to be unique (different than others in the list).
     */
    @FXML
    public void onSemesterNameChange() { // TODO data binding? how to check uniqueness
        String newName = semesterNameArea.getText();
        if (semester.getName().equals(newName)) {
            return;
        }
        if (studyGuideApplication.getStudyPlan().getSemesterPlan().getSemesterList().contains(new Semester(newName))) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    AbstractFXMLPane.messages.getString("semesterBox.nameNotUnique"));
            alert.showAndWait();
            semesterNameArea.setText(semester.getName());
        } else {
            semester.setName(newName);
        }
    }

    /**
     * Handles the start of a {@link com.oskopek.studyguide.model.courses.Course} drag and drop event.
     * Calls {@link SemesterController#dragDetected(SemesterBoxPane)}.
     */
    @FXML
    public void onDragDetected() {
        getParentController().dragDetected(this.viewElement);
        logger.debug("Drag detected {}", this.semester);
    }

    /**
     * Handles the end of a {@link com.oskopek.studyguide.model.courses.Course} drag and drop event.
     * Calls {@link SemesterController#dragEnded(SemesterBoxPane)}.
     */
    @FXML
    public void onDragDropped() {
        getParentController().dragEnded(this.viewElement);
        logger.debug("Drag dropped {}", this.semester);
    }

}
