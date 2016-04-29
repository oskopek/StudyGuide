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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CourseEnrollmentDetailController extends AbstractController {

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

    @FXML
    private void initialize() {
        course = new SimpleObjectProperty<>();
        creditsValueProperty = new CreditsStringProperty();
        teacherNamesProperty = new StringListStringProperty();
        prerequisitesProperty = new CourseListStringProperty();
        corequisitesProperty = new CourseListStringProperty();
    }


    public Course getCourse() {
        return course.get();
    }

    public void setCourse(Course newCourse) {
        this.course.set(newCourse);

        idField.textProperty().bindBidirectional(course.get().idProperty());
        nameField.textProperty().bindBidirectional(course.get().nameProperty());
        creditsValueProperty.bindBidirectional(course.get().creditsProperty(), creditsField.textProperty());
        teacherNamesProperty.bindBidirectional(course.get().teacherNamesProperty(), teacherNamesField.textProperty());
        corequisitesProperty.bindBidirectional(course.get().corequisitesProperty(), corequisitesField.textProperty());
        prerequisitesProperty.bindBidirectional(course.get().prerequisitesProperty(), prerequisitesField.textProperty());
    }

    private static class CourseListStringProperty {

        @Inject
        private CourseRegistry courseRegistry; // TODO check if this works (could there be more instances?)

        private ListProperty<Course> listProperty;

        private List<Course> list;
        private String stringList;

        private StringProperty stringProperty;

        public CourseListStringProperty() {
            // intentionally empty
        }

        public void bindBidirectional(ListProperty<Course> listProperty, StringProperty stringProperty) {
            this.listProperty = listProperty;
            this.stringProperty = stringProperty;

            listProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromList();
                }
            });
            stringProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromString();
                }
            });
        }

        private void synchronizeFromList() {
            synchronized (this) {
                stringList = list.stream().map(Course::getName).reduce(", ", String::join);
                stringProperty.setValue(stringList);
            }
        }

        private void synchronizeFromString() {
            synchronized (this) {
                list = Stream.of(stringList.split(",")).map(String::trim).map(id -> courseRegistry.getCourse(id)) // TODO check if valid correctly
                        .filter(x -> x != null).collect(Collectors.toList());
                listProperty.setValue(FXCollections.observableArrayList(list));
            }
        }

    }

    private static class CreditsStringProperty {

        private ObjectProperty<Credits> creditsProperty;

        private Credits credits;
        private String string;

        private StringProperty stringProperty;

        public CreditsStringProperty() {
            // intentionally empty
        }

        public void bindBidirectional(ObjectProperty<Credits> creditsProperty, StringProperty stringProperty) {
            this.creditsProperty = creditsProperty;
            this.stringProperty = stringProperty;

            creditsProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromCredits();
                }
            });
            stringProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromString();
                }
            });
        }

        private void synchronizeFromCredits() {
            synchronized (this) {
                string = Integer.toString(credits.getCreditValue());
                stringProperty.setValue(string);
            }
        }

        private void synchronizeFromString() {
            synchronized (this) {
                credits = Credits.valueOf(Integer.parseInt(string));
                creditsProperty.setValue(credits);
            }
        }

    }

    private static class StringListStringProperty {

        private ListProperty<String> listProperty;

        private List<String> list;
        private String string;

        private StringProperty stringProperty;

        public StringListStringProperty() {
            // intentionally empty
        }

        public void bindBidirectional(ListProperty<String> listProperty, StringProperty stringProperty) {
            this.listProperty = listProperty;
            this.stringProperty = stringProperty;

            listProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromList();
                }
            });
            stringProperty.addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    synchronizeFromString();
                }
            });
        }

        private void synchronizeFromList() {
            synchronized (this) {
                string = String.join(", ", list);
                stringProperty.setValue(string);
            }
        }

        private void synchronizeFromString() {
            synchronized (this) {
                list = Stream.of(string.split(",")).map(String::trim).collect(Collectors.toList());
                listProperty.setValue(FXCollections.observableArrayList(list));
            }
        }

    }
}
