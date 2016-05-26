package com.oskopek.studyguide.controller;

import com.google.common.eventbus.Subscribe;
import com.oskopek.studyguide.constraint.event.StringMessageEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.view.AlertCreator;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for SemesterBoxPane.
 * Represents a single {@link Semester} in a {@link com.oskopek.studyguide.model.SemesterPlan}.
 */
public class SemesterBoxController extends AbstractController {

    private static final DataFormat semesterFormat = new DataFormat("semester");
    private static final DataFormat enrollmentIndexFormat = new DataFormat("enrollment");
    private static final DecimalFormat coefficientFormatter = new DecimalFormat("#0.0");
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    private TextField semesterNameArea;
    @FXML
    private Label fulfilledTotalCreditLabel;
    @FXML
    private Label semesterDifficultyLabel;
    @FXML
    private TableView<CourseEnrollment> semesterTable;
    @FXML
    private TableColumn<CourseEnrollment, Label> warnColumn;
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
    private StringProperty fulfilledTotalCreditProperty;
    private StringProperty semesterDifficultyProperty;
    private BorderPane pane;

    /**
     * Initializes the listener for Semester name changes.
     *
     * @see #onSemesterNameChange()
     */
    @FXML
    private void initialize() {
        eventBus.register(this);
        coefficientFormatter.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US)); // use a dot "."
        fulfilledTotalCreditProperty = new SimpleStringProperty("0/0");
        semesterDifficultyProperty = new SimpleStringProperty("(0.0)");
        semesterNameArea.textProperty().addListener((observable) -> onSemesterNameChange());
        fulfilledTotalCreditLabel.textProperty().bind(fulfilledTotalCreditProperty);
        semesterDifficultyLabel.textProperty().bind(semesterDifficultyProperty);
        warnColumn.setCellValueFactory(param -> new WarningLabelBinding(param.getValue().brokenConstraintProperty()));
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getCourse().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getCourse().nameOrLocalizedNameProperty());
        creditsColumn
                .setCellValueFactory(cellData -> cellData.getValue().getCourse().getCredits().creditValueProperty());
        fulfilledColumn.setCellFactory(
                (final TableColumn<CourseEnrollment, Boolean> param) -> new TableCell<CourseEnrollment, Boolean>() {
                    public final CheckBox fulfilledCheckBox;

                    {
                        fulfilledCheckBox = new CheckBox();
                        fulfilledCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                            CourseEnrollment enrollment = getTableView().getItems().get(getIndex());
                            logger.trace("Setting isFulfilled to {} for Course Enrollment ({}) from Semester ({}).",
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
        removeColumn.setCellFactory(
                (final TableColumn<CourseEnrollment, String> param) -> new TableCell<CourseEnrollment, String>() {
                    final Button removeButton = new Button(messages.getString("crossmark"));

                    {
                        removeButton.setPadding(new Insets(2));
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            removeButton.setOnAction((ActionEvent event) -> {
                                CourseEnrollment enrollment = getTableView().getItems().get(getIndex());
                                logger.debug("Removing Course Enrollment ({}) from Semester ({}).", enrollment,
                                        semester);
                                semester.removeCourseEnrollment(enrollment);
                                studyGuideApplication.getStudyPlan().getConstraints()
                                        .removeAllCourseEnrollmentConstraints(enrollment);

                            });
                            setGraphic(removeButton);
                        }
                    }



                });
        semesterTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldEnrollment, newEnrollment) -> {
                    logger.debug("Focused on CourseEnrollment {}", newEnrollment);
                    Course toSet = null;
                    if (newEnrollment != null) {
                        toSet = newEnrollment.getCourse();
                    }
                    courseDetailController.setCourse(toSet);
                });
        updateSummaryLabels();
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
        updateSummaryLabels();
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
            AlertCreator.showAlert(Alert.AlertType.WARNING, messages.getString("semesterBox.nameNotUnique"));
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
     * An {@link com.google.common.eventbus.EventBus} subscriber,
     * listening for changes in a {@link Course}.
     *
     * @param changed the changed course posted on the bus
     */
    @Subscribe
    public void handleCourseChange(Course changed) {
        logger.trace("Course changed: {}", changed);
        updateSummaryLabels();
    }

    /**
     * An {@link com.google.common.eventbus.EventBus} subscriber,
     * listening for changes in a {@link CourseEnrollment}.
     *
     * @param changed the changed course enrollment posted on the bus
     */
    @Subscribe
    public void handleCourseEnrollmentChange(CourseEnrollment changed) {
        logger.trace("CourseEnrollment changed: {}", changed);
        if (semester.equals(changed.getSemester())) {
            updateSummaryLabels();
        }
    }

    /**
     * Updates the {@link #fulfilledTotalCreditLabel} and {@link #semesterDifficultyLabel}.
     */
    private void updateSummaryLabels() {
        if (semester == null) {
            fulfilledTotalCreditProperty.setValue("0/0");
            semesterDifficultyLabel.setTextFill(Color.BLACK);
            semesterDifficultyProperty.setValue("(0.0)");
            return;
        }
        int fulfilledSum = semester.getCourseEnrollmentList().stream().filter(CourseEnrollment::isFulfilled).collect(
                Collectors.summingInt(value -> value.getCourse().getCredits().getCreditValue()));
        int totalSum = semester.getCourseEnrollmentList().stream().collect(
                Collectors.summingInt(value -> value.getCourse().getCredits().getCreditValue()));

        fulfilledTotalCreditProperty.setValue(fulfilledSum + "/" + totalSum);
        double workloadCoefficient = ECTSWorkloadCalculator.calculateWorkloadCoefficient(semester);
        semesterDifficultyProperty.setValue("(" + coefficientFormatter.format(workloadCoefficient) + ")");
        semesterDifficultyLabel.setTextFill(ECTSWorkloadCalculator.calculateWorkloadColor(workloadCoefficient));
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
            Optional<Semester> semesterFrom = studyGuideApplication.getStudyPlan().getSemesterPlan()
                    .findSemester(semesterName);
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
        if (event.getGestureSource() != pane && event.getDragboard().hasContent(enrollmentIndexFormat) && event
                .getDragboard().hasContent(semesterFormat)) {
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

    /**
     * Utility class to calculate a workload coefficient for a given semester based on ECTS credit hour workload
     * estimates. Uses default for the Czech Republic. More info:
     * <a href="https://en.wikipedia.org/wiki/European_Credit_Transfer_and_Accumulation_System">https://en.wikipedia
     * .org/wiki/European_Credit_Transfer_and_Accumulation_System</a>
     */
    private static class ECTSWorkloadCalculator {

        //
        private static final int HOURS_PER_CREDIT = 26;
        private static final int SEMESTER_LENGTH = 14; // estimate

        /**
         * Calculates the workload coefficient: {@code (credit_sum * hoursSpentPerCredit) / semesterWeekCount / 7
         * (weekdays) / 24 (hours a day) * 100}.
         *
         * @param semester the semester whose coefficient to calculate
         * @return the calculated coefficient
         */
        public static double calculateWorkloadCoefficient(Semester semester) {
            int creditSum = semester.getCourseEnrollmentList().stream()
                    .collect(Collectors.summingInt(c -> c.getCourse().getCredits().getCreditValue()));
            return (creditSum * HOURS_PER_CREDIT) / (double) SEMESTER_LENGTH / 7d / 24d * 100d;
        }

        /**
         * Calculates a color (used for the label's text fill) representing the difficulty of the given semester.
         * Gradually increases from light green to dark red as the semester gets harder.
         *
         * @param workloadCoefficient the coefficient whose color to compute (should be in the range 0-60)
         * @return the calculated color
         */
        public static Color calculateWorkloadColor(double workloadCoefficient) {
            if (workloadCoefficient >= 60) { // set a max value
                workloadCoefficient = 60;
            }
            if (workloadCoefficient < 0) { // set a min value
                workloadCoefficient = 0;
            }
            // http://stackoverflow.com/questions/340209/generate-colors-between-red-and-green-for-a-power-meter
            int R = (int) Math.round((255 * workloadCoefficient) / 60d);
            int G = (int) Math.round((255 * (60 - workloadCoefficient)) / 60d);
            int B = 0;
            return Color.rgb(R, G, B).brighter();
        }
    }

    /**
     * Custom label binding, used for showing constraint warnings for any broken constraint event.
     */
    private class WarningLabelBinding extends ObjectBinding<Label> {

        private final ObjectProperty<? extends StringMessageEvent> eventObjectProperty;

        /**
         * Construct a WarningLabelBinding and bind to an event property.
         *
         * @param eventObjectProperty the property to bind to
         */
        WarningLabelBinding(ObjectProperty<? extends StringMessageEvent> eventObjectProperty) {
            bind(eventObjectProperty);
            this.eventObjectProperty = eventObjectProperty;
        }

        @Override
        protected Label computeValue() {
            Label label = new Label();
            if (eventObjectProperty.get() != null) {
                label.setBackground(
                        new Background(new BackgroundFill(Color.valueOf("red"), CornerRadii.EMPTY, Insets.EMPTY)));
                label.setText(messages.getString("warning"));
                label.setTooltip(new Tooltip(eventObjectProperty.get().message()));
            }
            return label;
        }
    }
}
