package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import com.oskopek.studyguide.model.courses.Credits;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller for displaying selected course details (and editing them).
 */
@Singleton
public class CourseDetailController extends AbstractController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField creditsField;

    @FXML
    private TextField teacherNamesField;

    @FXML
    private TextField prerequisitesField;

    @FXML
    private TextField corequisitesField;

    private CreditsStringProperty creditsValueProperty;

    private StringListStringProperty teacherNamesProperty;

    private CourseListStringProperty prerequisitesProperty;

    private CourseListStringProperty corequisitesProperty;

    private ObjectProperty<Course> course;

    /**
     * Initializes the panel's properties. They are bound in {@link #setCourse(Course)}.
     */
    @FXML
    private void initialize() {
        course = new SimpleObjectProperty<>();
        creditsValueProperty = new CreditsStringProperty();
        teacherNamesProperty = new StringListStringProperty();
        prerequisitesProperty = new CourseListStringProperty();
        corequisitesProperty = new CourseListStringProperty();
    }

    /**
     * Gets the course that is currently displayed.
     *
     * @return the shown course
     */
    public Course getCourse() {
        return course.get();
    }

    /**
     * Sets the current shown course and binds the text properties of all the corresponding {@link TextField}s.
     *
     * @param newCourse the course to set
     */
    public void setCourse(Course newCourse) {
        if (course.get() != null) {
            idField.textProperty().unbindBidirectional(course.get().idProperty());
            nameField.textProperty().unbindBidirectional(course.get().nameOrLocalizedNameProperty());
            creditsValueProperty.unbindBidirectional();
            teacherNamesProperty.unbindBidirectional();
            corequisitesProperty.unbindBidirectional();
            prerequisitesProperty.unbindBidirectional();
        }

        this.course.set(newCourse);
        logger.debug("New Course: {}, Course set: {}", newCourse, course.get());
        if (course.get() == null) {
            nameField.setText("");
            nameField.setDisable(true);
            creditsField.setText("");
            creditsField.setDisable(true);
            teacherNamesField.setText("");
            teacherNamesField.setDisable(true);
            prerequisitesField.setText("");
            prerequisitesField.setDisable(true);
            corequisitesField.setText("");
            corequisitesField.setDisable(true);
        } else {
            nameField.setDisable(false);
            creditsField.setDisable(false);
            teacherNamesField.setDisable(false);
            prerequisitesField.setDisable(false);
            corequisitesField.setDisable(false);

            idField.textProperty().bindBidirectional(course.get().idProperty());
            nameField.textProperty().bindBidirectional(course.get().nameOrLocalizedNameProperty());
            creditsValueProperty.bindBidirectional(course.get().creditsProperty(), creditsField.textProperty());
            teacherNamesProperty.bindBidirectional(course.get().teacherNamesProperty(),
                    teacherNamesField.textProperty());
            corequisitesProperty.bindBidirectional(studyGuideApplication.getStudyPlan().getCourseRegistry(),
                    course.get().corequisitesProperty(), corequisitesField.textProperty());
            prerequisitesProperty.bindBidirectional(studyGuideApplication.getStudyPlan().getCourseRegistry(),
                    course.get().prerequisitesProperty(), prerequisitesField.textProperty());
        }
    }

    /**
     * Custom-made property for bi-directional binding of properties of differing types:
     * {@code List<}{@link Course}{@code >} and {@link String}.
     */
    private static class CourseListStringProperty {

        private CourseRegistry courseRegistry;

        private Logger logger = LoggerFactory.getLogger(getClass());

        private ListProperty<Course> listProperty;
        private StringProperty stringProperty;
        private ChangeListener<List<Course>> listListener;
        private ChangeListener<String> stringListener;

        /**
         * Default constructor.
         */
        CourseListStringProperty() {
            listListener = (observable, oldValue, newValue) -> {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    synchronizeFromList(newValue);
                }
            };
            stringListener = (observable, oldValue, newValue) -> {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    synchronizeFromString(newValue);
                }
            };
        }

        /**
         * Bind a pair of type-differing JavaFX properties bidirectionally. Keeps the two properties internally
         * and synchronizes them via {@link javafx.beans.value.ChangeListener}s.
         * The String property is a comma-separated list of {@link Course#getName()}s from the list.
         *
         * @param registry the course registry from which to recognize inputted courses
         * @param listProperty the list property to bind
         * @param stringProperty the string property to bind
         */
        public void bindBidirectional(CourseRegistry registry, ListProperty<Course> listProperty,
                                      StringProperty stringProperty) {
            this.courseRegistry = registry;
            this.listProperty = listProperty;
            this.stringProperty = stringProperty;
            this.listProperty.addListener(listListener);
            this.stringProperty.addListener(stringListener);
            listListener.changed(listProperty, null, listProperty.get());
        }

        /**
         * Removes the binding and removes all listeners.
         */
        public void unbindBidirectional() {
            courseRegistry = null;
            listProperty.removeListener(listListener);
            listProperty = null;
            stringProperty.removeListener(stringListener);
            stringProperty = null;
        }

        /**
         * Computes a new String representation of the List. Called on a List change.
         *
         * @param list the new list value
         */
        private void synchronizeFromList(List<Course> list) {
            logger.debug("Synchronizing from list {} using course registry {}", list, courseRegistry);
            String stringList = String.join(", ", list.stream().map(Course::getId).collect(Collectors.toList()));
            stringProperty.setValue(stringList);
            logger.debug("Synchronized to stringList {}", stringList);

        }

        /**
         * Computes a new List representation of the String. Called on a String change.
         * This transitively applies changes to the bound list property.
         *
         * @param stringList the new stringList value
         */
        private void synchronizeFromString(String stringList) {
            if (stringList == null) {
                return; // TODO OPTIONAL check if valid correctly
            }
            logger.debug("Synchronizing from stringList {} using course registry {}", stringList, courseRegistry);
            List<Course> list = Stream.of(stringList.split(",")).map(String::trim)
                    .map(id -> courseRegistry.getCourse(id)).filter(x -> x != null).collect(Collectors.toList());
            listProperty.setValue(FXCollections.observableArrayList(list));
            logger.debug("Synchronized to list {}", list);
        }

    }

    /**
     * Custom-made property for bi-directional binding of properties of differing types:
     * {@link Credits} and {@link String}.
     */
    private static class CreditsStringProperty {

        private ObjectProperty<Credits> creditsProperty;
        private StringProperty stringProperty;
        private ChangeListener<Credits> creditsListener;
        private ChangeListener<String> stringListener;

        @Inject
        private Logger logger;

        /**
         * Default constructor.
         */
        CreditsStringProperty() {
            creditsListener = (observable, oldValue, newValue) -> {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    synchronizeFromCredits(newValue);
                }
            };
            stringListener = (observable, oldValue, newValue) -> {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    synchronizeFromString(newValue);
                }
            };
        }

        /**
         * Bind a pair of type-differing JavaFX properties bidirectionally. Keeps the two properties internally
         * and synchronizes them via {@link javafx.beans.value.ChangeListener}s and intermediate value caches.
         * The String property is textual representation of the value of the Credits.
         *
         * @param creditsProperty the {@link Credits} property to bind
         * @param stringProperty the string property to bind
         */
        public void bindBidirectional(ObjectProperty<Credits> creditsProperty, StringProperty stringProperty) {
            this.creditsProperty = creditsProperty;
            this.stringProperty = stringProperty;
            this.creditsProperty.addListener(creditsListener);
            this.stringProperty.addListener(stringListener);
            creditsListener.changed(creditsProperty, null, creditsProperty.get());
        }

        /**
         * Removes the binding and removes all listeners.
         */
        public void unbindBidirectional() {
            creditsProperty.removeListener(creditsListener);
            creditsProperty = null;
            stringProperty.removeListener(stringListener);
            stringProperty = null;
        }

        /**
         * Computes a new String representation of the Credits. Called on a Credits change.
         *
         * @param credits the new credits value
         */
        private void synchronizeFromCredits(Credits credits) {
            String string = Integer.toString(credits.getCreditValue());
            stringProperty.setValue(string);
        }

        /**
         * Computes a new Credits representation of the String. Called on a String change.
         * This transitively applies changes to the bound object property.
         *
         * @param string the new string value
         */
        private void synchronizeFromString(String string) {
            int val;
            try {
                val = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                logger.info("Wrong number format ({}), ignoring the credit value.", string);
                return; // TODO OPTIONAL do not ignore the wrong value
            }
            Credits credits = Credits.valueOf(val);
            creditsProperty.setValue(credits);
        }
    }

    /**
     * Custom-made property for bi-directional binding of properties of differing types:
     * {@code List<}{@link String}{@code >} and {@link String}.
     */
    private static class StringListStringProperty {

        private ListProperty<String> listProperty;
        private StringProperty stringProperty;
        private ChangeListener<List<String>> listListener;
        private ChangeListener<String> stringListener;

        /**
         * Default constructor.
         */
        StringListStringProperty() {
            listListener = ((observable, oldValue, newValue) -> {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    synchronizeFromList(newValue);
                }
            });
            stringListener = (observable, oldValue, newValue) -> {
                if (oldValue == null || !oldValue.equals(newValue)) {
                    synchronizeFromString(newValue);
                }
            };
        }

        /**
         * Bind a pair of type-differing JavaFX properties bidirectionally. Keeps the two properties internally
         * and synchronizes them via {@link javafx.beans.value.ChangeListener}s.
         * The String property is a comma-separated list of values from the list.
         *
         * @param listProperty the list property to bind
         * @param stringProperty the string property to bind
         */
        public void bindBidirectional(ListProperty<String> listProperty, StringProperty stringProperty) {
            this.listProperty = listProperty;
            this.stringProperty = stringProperty;
            this.listProperty.addListener(listListener);
            this.stringProperty.addListener(stringListener);
            listListener.changed(listProperty, null, listProperty.get());
        }

        /**
         * Removes the binding and removes all listeners.
         */
        public void unbindBidirectional() {
            listProperty.removeListener(listListener);
            listProperty = null;
            stringProperty.removeListener(stringListener);
            stringProperty = null;
        }

        /**
         * Computes a new String representation of the List. Called on a List change.
         *
         * @param list the new list value
         */
        private void synchronizeFromList(List<String> list) {
            String string = String.join(", ", list);
            stringProperty.setValue(string);
        }

        /**
         * Computes a new List representation of the String. Called on a String change.
         * This transitively applies changes to the bound list property.
         *
         * @param string the new string value
         */
        private void synchronizeFromString(String string) {
            if (string == null) {
                return;
            }
            List<String> list = Stream.of(string.split(",")).map(String::trim).collect(Collectors.toList());
            listProperty.setValue(FXCollections.observableArrayList(list));
        }
    }

}
