package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.view.ChooseCourseDialogPane;
import com.oskopek.studyguide.view.FindCoursePane;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for searching courses in multiple data-sources.
 */
public class FindCoursesController extends AbstractController<FindCoursePane> implements FindCourses {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TextField searchField;

    private List<FindCourses> findCoursesList;

    /**
     * Creates an empty instance.
     */
    public FindCoursesController() {
        this.findCoursesList = new ArrayList<>();
    }

    /**
     * Handles the user action of searching for a course.
     */
    @FXML
    public void handleSearch() {
        String input = searchField.getText();
        List<Course> courses = findCourses(input).collect(Collectors.toList());
        logger.debug("Courses found for input \"{}\": {}", input, Arrays.toString(courses.toArray()));

        ChooseCourseDialogPane pane = new ChooseCourseDialogPane();
        Dialog<ButtonType> chooseCourseDialog = new Dialog<>();
        chooseCourseDialog.dialogPaneProperty()
                .setValue((DialogPane) pane.load(studyGuideApplication, courses, chooseCourseDialog));
        ChooseCourseController controller = (ChooseCourseController) pane.getController();

        Optional<ButtonType> result = chooseCourseDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            Course chosen = controller.getChosenCourse();
            if (chosen != null) {
                logger.debug("Chosen course: {}", chosen);
                // TODO add chosen course to semester
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
    public void reinitialize() {
        findCoursesList.clear();
        findCoursesList.add(new FindRegistryCoursesController(
                studyGuideApplication.getStudyPlan().getCourseRegistry()));
    }

}
