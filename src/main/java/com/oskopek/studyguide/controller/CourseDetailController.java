package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import com.oskopek.studyguide.model.courses.Credits;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
        this.course.set(newCourse);
        if (course == null) {
            nameField.setDisable(true);
            creditsField.setDisable(true);
            teacherNamesField.setDisable(true);
            prerequisitesField.setDisable(true);
            corequisitesField.setDisable(true);
        } else {
            nameField.setDisable(false);
            creditsField.setDisable(false);
            teacherNamesField.setDisable(false);
            prerequisitesField.setDisable(false);
            corequisitesField.setDisable(false);


            idField.textProperty().bindBidirectional(course.get().idProperty());
            nameField.textProperty().bindBidirectional(course.get().nameProperty());
            creditsValueProperty.bindBidirectional(course.get().creditsProperty(), creditsField.textProperty());
            teacherNamesProperty.bindBidirectional(course.get().teacherNamesProperty(),
                    teacherNamesField.textProperty());
            corequisitesProperty.bindBidirectional(course.get().corequisitesProperty(),
                    corequisitesField.textProperty());
            prerequisitesProperty.bindBidirectional(course.get().prerequisitesProperty(),
                    prerequisitesField.textProperty());
        }
    }

    /**
     * Custom-made property for bi-directional binding of properties of differing types:
     * {@code List<}{@link Course}{@code >} and {@link String}.
     */
    private static class CourseListStringProperty {

        @Inject
        private CourseRegistry courseRegistry; // TODO check if this works (could there be more instances?)

        private ListProperty<Course> listProperty;
        private StringProperty stringProperty;

        /**
         * Bind a pair of type-differing JavaFX properties bidirectionally. Keeps the two properties internally
         * and synchronizes them via {@link javafx.beans.value.ChangeListener}s.
         * The String property is a comma-separated list of {@link Course#getName()}s from the list.
         *
         * @param listProperty   the list property to bind
         * @param stringProperty the string property to bind
         */
        public void bindBidirectional(ListProperty<Course> listProperty, StringProperty stringProperty) {
            this.listProperty = listProperty;
            this.stringProperty = stringProperty;

            listProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromList(newValue);
                }
            });
            stringProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromString(newValue);
                }
            });
        }

        /**
         * Computes a new String representation of the List. Called on a List change.
         *
         * @param list the new list value
         */
        private void synchronizeFromList(List<Course> list) {
            String stringList = list.stream().map(Course::getName).reduce(", ", String::join);
            stringProperty.setValue(stringList);
        }

        /**
         * Computes a new List representation of the String. Called on a String change.
         * This transitively applies changes to the bound list property.
         *
         * @param stringList the new stringList value
         */
        private void synchronizeFromString(String stringList) {
            // TODO check if valid correctly
            List<Course> list = Stream.of(stringList.split(",")).map(String::trim)
                    .map(id -> courseRegistry.getCourse(id)).filter(x -> x != null).collect(Collectors.toList());
            listProperty.setValue(FXCollections.observableArrayList(list));
        }

    }

    /**
     * Custom-made property for bi-directional binding of properties of differing types:
     * {@link Credits} and {@link String}.
     */
    private static class CreditsStringProperty {

        private ObjectProperty<Credits> creditsProperty;
        private StringProperty stringProperty;

        /**
         * Bind a pair of type-differing JavaFX properties bidirectionally. Keeps the two properties internally
         * and synchronizes them via {@link javafx.beans.value.ChangeListener}s and intermediate value caches.
         * The String property is textual representation of the value of the Credits.
         *
         * @param creditsProperty the {@link Credits} property to bind
         * @param stringProperty  the string property to bind
         */
        public void bindBidirectional(ObjectProperty<Credits> creditsProperty, StringProperty stringProperty) {
            this.creditsProperty = creditsProperty;
            this.stringProperty = stringProperty;

            creditsProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromCredits(newValue);
                }
            });
            stringProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromString(newValue);
                }
            });
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
                return; // TODO do not ignore the wrong value
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

        /**
         * Bind a pair of type-differing JavaFX properties bidirectionally. Keeps the two properties internally
         * and synchronizes them via {@link javafx.beans.value.ChangeListener}s.
         * The String property is a comma-separated list of values from the list.
         *
         * @param listProperty   the list property to bind
         * @param stringProperty the string property to bind
         */
        public void bindBidirectional(ListProperty<String> listProperty, StringProperty stringProperty) {
            this.listProperty = listProperty;
            this.stringProperty = stringProperty;

            listProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromList(newValue);
                }
            });
            stringProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromString(newValue);
                }
            });
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
