package com.oskopek.studyguide.model.constraints;

import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Defines a group of courses that can be validated against a {@link com.oskopek.studyguide.constraint.Constraint}.
 */
public class CourseGroup {

    private final ListProperty<Course> courseList;

    /**
     * Builds a new, non-empty course group.
     *
     * @param courseList          non-empty list of courses
     * @throws IllegalArgumentException if any parameter is null or any list is empty
     */
    public CourseGroup(List<Course> courseList) throws IllegalArgumentException {
        if (courseList == null || courseList.isEmpty()) {
            throw new IllegalArgumentException("The parameters cannot be null and the lists cannot be empty.");
        }
        this.courseList = new SimpleListProperty<>(FXCollections.observableArrayList(courseList));
    }

    /**
     * The list property of courses in this group.
     *
     * @return non-null, not empty
     */
    public ReadOnlyListProperty<Course> courseListProperty() {
        return courseList;
    }

    /**
     * The internal course list.
     *
     * @return non-null list of {@link Course}s
     */
    private ObservableList<Course> getCourseList() {
        return courseList.get();
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseList the list of {@link Course}s to set
     */
    private void setCourseList(List<Course> courseList) {
        this.courseList.set(FXCollections.observableArrayList(courseList));
    }
}
