package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.view.AlertCreator;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Controller for SemesterBoxPane.
 * Represents a single {@link Semester} in a {@link com.oskopek.studyguide.model.SemesterPlan}
 *
 */
public class SemesterBoxController extends AbstractController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static int index = 0;

    @FXML
    private TextField semesterNameArea;

    @FXML
    private TableView<CourseEnrollment> semesterTable;

    @FXML
    private TableColumn<CourseEnrollment, String> idColumn;

    @FXML
    private TableColumn<CourseEnrollment, String> nameColumn;

    @FXML
    private TableColumn<CourseEnrollment, Number> creditsColumn;

    @FXML
    private TableColumn<CourseEnrollment, Boolean> fulfilledColumn;

    @FXML
    private TableColumn<CourseEnrollment, String> removeColumn;

    @Inject
    private SemesterController parentSemesterController;

    @Inject
    private CourseEnrollmentDetailController courseEnrollmentDetailController;

    private Semester semester;

    /**
     * Initializes the listener for Semester name changes.
     *
     * @see #onSemesterNameChange()
     */
    @FXML
    private void initialize() {
        semesterNameArea.textProperty().addListener((observable) -> onSemesterNameChange());

        idColumn.setCellValueFactory(cellData -> cellData.getValue().getCourse().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getCourse().nameProperty());
        creditsColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCourse().getCredits().creditValueProperty());
        fulfilledColumn.setCellFactory(CheckBoxTableCell.forTableColumn(fulfilledColumn)); // TODO make editable
        fulfilledColumn.setCellValueFactory(cellData -> cellData.getValue().fulfilledProperty());
        removeColumn.setCellFactory((final TableColumn<CourseEnrollment, String> param) ->
                new TableCell<CourseEnrollment, String>() {
                    final Button removeButton = new Button("✗");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            removeButton.setOnAction((ActionEvent event) -> {
                                CourseEnrollment enrollment = getTableView().getItems().get(getIndex());
                                semester.removeCourseEnrollment(enrollment);
                                logger.debug("Removing Course Enrollment ({}) from Semester ({}).",
                                        enrollment, semester);
                            });
                            setGraphic(removeButton);
                        }
                    }
                });

        EventHandler<Event> d = event -> { // TODO change to on focus
            CourseEnrollment e = semesterTable.getSelectionModel().getSelectedItem();
            logger.debug("Focused on CourseEnrollment {}", e);
            courseEnrollmentDetailController.setCourse(e.getCourse());
        };
        semesterTable.setOnMouseClicked(d);
        semesterTable.setOnKeyReleased(d);
    }

    /**
     * Set the semester into this box. Does not update the model.
     *
     * @param semester non-null
     */
    public void setSemester(Semester semester) {
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null.");
        }
        this.semester = semester;
        semesterNameArea.setText(semester.getName());
        semesterTable.itemsProperty().bindBidirectional(semester.courseEnrollmentListProperty());
    }

    /**
     * Get the parent controller. Used for inter-semester data exchange.
     *
     * @return non-null controller for SemesterPane
     * @see #onRemoveSemester()
     */
    private SemesterController getParentController() {
        return parentSemesterController;
    }

    /**
     * Handles removing this whole box. Calls {@link SemesterController#removeSemester(Semester)}.
     */
    @FXML
    public void onRemoveSemester() {
        semesterTable.itemsProperty().unbindBidirectional(semester.courseEnrollmentListProperty());
        parentSemesterController.removeSemester(semester);
    }

    /**
     * Handles changing the name of this semester box. The name has to be unique (different than others in the list).
     */
    @FXML
    public void onSemesterNameChange() {
        String newName = semesterNameArea.getText();
        if (semester.getName().equals(newName)) {
            return;
        }
        if (studyGuideApplication.getStudyPlan().getSemesterPlan().getSemesterList().contains(new Semester(newName))) {
            AlertCreator.showAlert(Alert.AlertType.WARNING,
                    messages.getString("semesterBox.nameNotUnique"));
            semesterNameArea.setText(semester.getName());
        } else {
            semester.setName(newName);
        }
    }

}
