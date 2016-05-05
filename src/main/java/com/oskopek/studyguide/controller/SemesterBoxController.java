package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.view.AlertCreator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Controller for SemesterBoxPane.
 * Represents a single {@link Semester} in a {@link com.oskopek.studyguide.model.SemesterPlan}.
 */
public class SemesterBoxController extends AbstractController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    private CourseDetailController courseDetailController;

    private Semester semester;

    private BorderPane pane;

    private static final DataFormat semesterFormat = new DataFormat("semester");
    private static final DataFormat enrollmentIndexFormat = new DataFormat("enrollment");

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
        fulfilledColumn.setCellFactory((final TableColumn<CourseEnrollment, Boolean> param) ->
                new TableCell<CourseEnrollment, Boolean>() {
                    public final CheckBox fulfilledCheckBox;

                    {
                        fulfilledCheckBox = new CheckBox();
                        fulfilledCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            CourseEnrollment enrollment = getTableView().getItems().get(getIndex());
                            logger.debug("Setting isFulfilled to {} for Course Enrollment ({}) from Semester ({}).",
                                    newValue, enrollment, semester);
                            enrollment.setFulfilled(newValue);
                            fulfilledCheckBox.setSelected(newValue);
                        });
                    }

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            fulfilledCheckBox.setSelected(item);
                            setGraphic(fulfilledCheckBox);
                        }
                    }
                });
        fulfilledColumn.setCellValueFactory(cellData -> cellData.getValue().fulfilledProperty());
        removeColumn.setCellFactory((final TableColumn<CourseEnrollment, String> param) ->
                new TableCell<CourseEnrollment, String>() {
                    final Button removeButton = new Button(messages.getString("crossmark"));

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            removeButton.setOnAction((ActionEvent event) -> {
                                CourseEnrollment enrollment = getTableView().getItems().get(getIndex());
                                logger.debug("Removing Course Enrollment ({}) from Semester ({}).",
                                        enrollment, semester);
                                semester.removeCourseEnrollment(enrollment);

                            });
                            setGraphic(removeButton);
                        }
                    }
                });
        semesterTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldEnrollment, newEnrollment) -> {
                    logger.debug("Focused on CourseEnrollment {}", newEnrollment);
                    Course toSet = null;
                    if (newEnrollment != null) {
                        toSet = newEnrollment.getCourse();
                    }
                    courseDetailController.setCourse(toSet);
                });
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

    /**
     * Get the {@link BorderPane} UI element that this controller controls.
     *
     * @return the border pane to get
     */
    public BorderPane getPane() {
        return pane;
    }

    /**
     * Set the {@link BorderPane} UI element that this controller controls.
     * Used by {@link com.oskopek.studyguide.view.SemesterBoxPaneCreator}.
     *
     * @param pane the border pane to set
     */
    public void setPane(BorderPane pane) {
        this.pane = pane;
    }

    /**
     * Handler of finished drag in the table.
     *
     * @param e the drag event
     */
    @FXML
    private void onDragDropped(DragEvent e) {
        Semester semesterTo = semester;
        logger.trace("Drag dropped in {}", semester);
        Dragboard dragboard = e.getDragboard();
        if (dragboard.hasContent(semesterFormat) && dragboard.hasContent(enrollmentIndexFormat)) {
            String semesterName = (String) dragboard.getContent(semesterFormat);
            int selectedIndex = (Integer) dragboard.getContent(enrollmentIndexFormat);
            logger.debug("Drag dropping payload: {} from {} to {}", selectedIndex, semesterName, semesterTo.getName());
            Optional<Semester> semesterFrom
                    = studyGuideApplication.getStudyPlan().getSemesterPlan().findSemester(semesterName);
            if (!semesterFrom.isPresent()) {
                throw new IllegalStateException("No such semester exists! Cannot drop the drag.");
            }
            CourseEnrollment enrollment = semesterFrom.get().getCourseEnrollmentList().get(selectedIndex);
            parentSemesterController.moveCourseEnrollment(semesterFrom.get(), semesterTo, enrollment);
        }
        e.consume();
    }

    /**
     * Handler of dragging an enrollment over a table. Used for accepting/rejecting the payload.
     *
     * @param event the drag event
     */
    @FXML
    private void onDragOver(DragEvent event) {
        logger.trace("Drag over in {}", semester);
        if (event.getGestureSource() != pane && event.getDragboard().hasContent(enrollmentIndexFormat)
                && event.getDragboard().hasContent(semesterFormat)) {
            event.acceptTransferModes(TransferMode.MOVE);
            logger.trace("Accepting drag over in {}", semester);
        }
        event.consume();
    }

    /**
     * Handler of detected drag in the table.
     *
     * @param event the event that initiated the drag
     */
    @FXML
    private void onDragDetected(MouseEvent event) {
        int selectedIndex = semesterTable.getSelectionModel().getSelectedIndex();
        logger.debug("Drag detected in {}", semester);
        if (selectedIndex >= 0) {
            logger.debug("Drag detected: {} from {}.", selectedIndex, semester);
            Dragboard dragboard = pane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.put(semesterFormat, semester.getName());
            clipboardContent.put(enrollmentIndexFormat, selectedIndex);
            dragboard.setContent(clipboardContent);
        }
        event.consume();
    }
}
