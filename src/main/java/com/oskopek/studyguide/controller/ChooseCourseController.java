package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;
import com.oskopek.studyguide.view.ChooseCourseDialogPane;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for searching courses in multiple data-sources.
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
    private TableColumn<Course, Credits> creditsColumn;

    private List<Course> courseList;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getId());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getLocalizedName());
        creditsColumn.setCellValueFactory(cellData -> cellData.getValue().getCredits());
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    /**
     * Handles the user action of choosing a course.
     */
    @FXML
    public void handleChoice() {
        Course chosen = courseTableView.getSelectionModel().getSelectedItem();
        // TODO add the choice to a (the latest) semester
        logger.debug("Chosen course: {}", chosen);
    }
}
