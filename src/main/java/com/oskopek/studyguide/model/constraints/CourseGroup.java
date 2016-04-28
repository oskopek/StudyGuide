package com.oskopek.studyguide.model.constraints;

import com.oskopek.studyguide.constraints.CourseGroupConstraint;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseGroupType;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Defines a group of courses that can be validated against a {@link com.oskopek.studyguide.constraints.Constraint}.
 */
public class CourseGroup {

    private ObjectProperty<CourseGroupType> courseGroupType;
    private ListProperty<Course> courseList;
    private ListProperty<CourseGroupConstraint> courseGroupConstraintList;

    /**
     * Builds a new, non-empty course group.
     *
     * @param courseGroupType     the type of courses in this course
     * @param courseList          non-empty list of courses
     * @param courseGroupConstraintList non-empty list of constraints
     * @throws IllegalArgumentException if any parameter is null or any list is empty
     */
    public CourseGroup(CourseGroupType courseGroupType, List<Course> courseList,
                       List<CourseGroupConstraint> courseGroupConstraintList) throws IllegalArgumentException {
        if (courseGroupType == null || courseList == null || courseGroupConstraintList == null || courseList.isEmpty()
                || courseGroupConstraintList.isEmpty()) {
            throw new IllegalArgumentException("The parameters cannot be null and the lists cannot be empty.");
        }
        this.courseGroupType = new SimpleObjectProperty<>(courseGroupType);
        this.courseList = new SimpleListProperty<>(FXCollections.observableArrayList(courseList));
        this.courseGroupConstraintList
                = new SimpleListProperty<>(FXCollections.observableArrayList(courseGroupConstraintList));
    }

    /**
     * The type property of courses in this course group.
     *
     * @return non-null
     */
    public ObjectProperty<CourseGroupType> courseGroupTypeProperty() {
        return courseGroupType;
    }

    /**
     * The list property of courses in this group.
     *
     * @return non-null, not empty
     */
    public ListProperty<Course> courseListProperty() {
        return courseList;
    }

    /**
     * The list property of constraints imposed on this group.
     *
     * @return non-null, not empty
     */
    public ListProperty<CourseGroupConstraint> courseGroupConstraintListProperty() {
        return courseGroupConstraintList;
    }

    /**
     * The internal course group type.
     *
     * @return non-null {@link CourseGroupType} enum member
     */
    public CourseGroupType getCourseGroupType() {
        return courseGroupType.get();
    }

    /**
     * The internal course list.
     *
     * @return non-null list of {@link Course}s
     */
    public ObservableList<Course> getCourseList() {
        return courseList.get();
    }

    /**
     * The internal constraint list.
     *
     * @return non-null list of {@link CourseGroupConstraint}s
     */
    public ObservableList<CourseGroupConstraint> getCourseGroupConstraintList() {
        return courseGroupConstraintList.get();
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseGroupType the {@link CourseGroupType} to set
     */
    private void setCourseGroupType(CourseGroupType courseGroupType) {
        this.courseGroupType.set(courseGroupType);
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseList the list of {@link Course}s to set
     */
    private void setCourseList(List<Course> courseList) {
        this.courseList.set(FXCollections.observableArrayList(courseList));
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseGroupConstraintList the list of {@link CourseGroupConstraint}s to set
     */
    private void setCourseGroupConstraintList(List<CourseGroupConstraint> courseGroupConstraintList) {
        this.courseGroupConstraintList.set(FXCollections.observableArrayList(courseGroupConstraintList));
    }
}
