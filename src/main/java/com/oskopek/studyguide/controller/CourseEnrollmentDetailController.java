package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import com.oskopek.studyguide.model.courses.Credits;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Predicate;
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

    private StringProperty creditsValueProperty;

    private StringProperty teacherNamesProperty;

    private CourseListStringProperty prerequisitesProperty;

    private CourseListStringProperty corequisitesProperty;

    private ObjectProperty<Course> course;

    @FXML
    private void initialize() {
        course = new SimpleObjectProperty<>();
        creditsValueProperty = new SimpleStringProperty();
        teacherNamesProperty = new SimpleStringProperty();
        prerequisitesProperty = new CourseListStringProperty();
        corequisitesProperty = new CourseListStringProperty();



    }


    public Course getCourse() {
        return course.get();
    }

    public void setCourse(Course newCourse) {
        this.course.set(newCourse);

        creditsValueProperty.addListener((observable, oldValue, newValue)
                -> course.get().setCredits(Credits.valueOf(Integer.parseInt(newValue)))); // TODO check value is valid

        idField.textProperty().bindBidirectional(course.get().idProperty());
        nameField.textProperty().bindBidirectional(course.get().nameProperty());
        creditsField.textProperty().bindBidirectional(creditsValueProperty);
        corequisitesProperty.bindBidirectional(course.get().corequisitesProperty(), teacherNamesField.textProperty());

        this.creditsValueProperty.setValue(Integer.toString(course.get().getCredits().getCreditValue())); // TODO move me
        this.teacherNamesProperty.setValue(
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
                synchronizeFromList();
            });
            stringProperty.addListener((observable, oldValue, newValue) -> {
                if (oldValue.equals()) // TODO finished here
                synchronizeFromString();
            });
        }

        private void synchronizeFromList() {
            synchronized (this) {
                stringList = list.stream().map(c -> c.getName()).reduce(", ", String::join);
            }
        }

        private void synchronizeFromString() {
            synchronized (this) {
                list = Stream.of(stringList.split(",")).map(String::trim).map(id -> courseRegistry.getCourse(id))
                        .filter(x -> x != null).collect(Collectors.toList());
            }
        }

    }
}
