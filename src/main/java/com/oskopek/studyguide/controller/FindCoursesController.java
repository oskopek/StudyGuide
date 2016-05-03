package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.view.AlertCreator;
import com.oskopek.studyguide.view.ChooseCourseDialogPaneCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for searching courses in multiple data-sources.
 */
@Singleton
public class FindCoursesController extends AbstractController implements FindCourses {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TextField searchField;

    private List<FindCourses> findCoursesList;

    @Inject
    private ChooseCourseDialogPaneCreator chooseCourseDialogPaneCreator;

    /**
     * Creates an empty instance.
     */
    public FindCoursesController() {
        this.findCoursesList = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        studyGuideApplication.studyPlanProperty().addListener((observable, oldValue, newValue) -> {
            reinitialize(newValue);
        });
    }

    /**
     * Handles the user action of searching for a course.
     */
    @FXML
    public void handleSearch() {
        String input = searchField.getText();
        List<Course> courses = findCourses(input).collect(Collectors.toList());
        logger.debug("Courses found for input \"{}\": {}", input, Arrays.toString(courses.toArray()));

        if (courses.isEmpty()) {
            AlertCreator.showAlert(Alert.AlertType.INFORMATION, "No courses found for search string: \""
                    + input + "\"");
            return;
        }

        Dialog<ButtonType> chooseCourseDialog = new Dialog<>();
        ChooseCourseController controller = chooseCourseDialogPaneCreator.create(courses, chooseCourseDialog);

        Optional<ButtonType> result = chooseCourseDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            Course chosen = controller.getChosenCourse();
            if (chosen != null) {
                logger.debug("Chosen course: {}", chosen);
                try {
                    Semester addTo = studyGuideApplication.getStudyPlan().getSemesterPlan().lastSemester();
                    if (addTo == null) { // no semester in plan
                        AlertCreator.showAlert(Alert.AlertType.ERROR,
                                messages.getString("findCourses.noSemester"));
                    } else {
                        addTo.addCourseEnrollment(chosen);
                    }
                } catch (IllegalArgumentException e) {
                    logger.debug("Added wrong course({}), showing error box.", chosen);
                    AlertCreator.showAlert(Alert.AlertType.ERROR,
                            messages.getString("findCourses.courseAlreadyEnrolled"));
                }
            }
        }
    }

    /**
     * Add a {@link FindCourses} instance to the list of course data-sources.
     *
     * @param findCourses a non-null instance
     */
    public void addFindCourses(FindCourses findCourses) {
        findCoursesList.add(findCourses);
    }

    /**
     * Returns the top 10 distinct courses, using the search function on all {@link FindCourses} instances.
     * @param searchFunction the function from {@link FindCourses} to {@link Stream}s of {@link Course}s
     * @return top 10 list of distinct collected Courses
     */
    private List<Course> findCoursesInternal(Function<? super FindCourses, Stream<Course>> searchFunction) {
        return findCoursesList.parallelStream().flatMap(searchFunction)
                .distinct().limit(10).collect(Collectors.toList());
    }

    @Override
    public Stream<Course> findCourses(String key) {
        return findCoursesList.parallelStream().flatMap((FindCourses f) -> f.findCourses(key))
                .distinct().limit(10);
    }

    @Override
    public Stream<Course> findCoursesById(String id) {
        return findCoursesList.parallelStream().flatMap(f -> f.findCoursesById(id)).distinct().limit(10);
    }

    @Override
    public Stream<Course> findCoursesByName(String name, Locale locale) {
        return findCoursesList.parallelStream().flatMap(f -> f.findCoursesByName(name, locale)).distinct().limit(10);
    }

    /**
     * Clears the {@link #findCoursesList}
     * and adds the default {@link com.oskopek.studyguide.model.courses.CourseRegistry} from the model.
     */
    private void reinitialize(StudyPlan studyPlan) {
        findCoursesList.clear();
        if (studyPlan == null) {
            return;
        }
        findCoursesList.add(new FindRegistryCoursesController(
                studyPlan.getCourseRegistry()));
    }

}
