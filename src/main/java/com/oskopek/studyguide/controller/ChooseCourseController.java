package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.view.ChooseCourseDialogPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller for choosing a course out of several choices.
 */
public class ChooseCourseController extends AbstractController<ChooseCourseDialogPane> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TableView<Course> courseTableView;

    @FXML
    private TableColumn<Course, String> idColumn;

    @FXML
    private TableColumn<Course, String> nameColumn;

    @FXML
    private TableColumn<Course, Number> creditsColumn;

    @FXML
    private ButtonType applyButton;

    private ObservableList<Course> courseList;

    /**
     * Initialize the {@link #courseTableView} data bindings.
     */
    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        creditsColumn.setCellValueFactory(cellData -> cellData.getValue().getCredits().creditValueProperty());
    }

    /**
     * Sets the courseList displayed in the dialog.
     *
     * @param courseList non-null
     */
    public void setCourseList(List<Course> courseList) {
        this.courseList = FXCollections.observableList(courseList);
        courseTableView.setItems(this.courseList);
    }

    /**
     * Utility method for returning the selected course.
     *
     * @return selected course, or null if none selected
     */
    public Course getChosenCourse() {
        return courseTableView.getSelectionModel().getSelectedItem();
    }
}
