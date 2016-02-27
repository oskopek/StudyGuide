package com.oskopek.studyguide.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Single semester in a {@link StudyPlan}.
 */
public class Semester {

    private StringProperty name;
    private ListProperty<CourseEnrollment> courseEnrollmentList;

    /**
     * Create an empty semester.
     *
     * @param name unique, non-null
     * @throws IllegalArgumentException if name is null
     */
    public Semester(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        this.name = new SimpleStringProperty(name);
        this.courseEnrollmentList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * The unique name of the semester.
     *
     * @return non-null
     */
    public String getName() {
        return name.get();
    }

    /**
     * The unique name of the semester.
     *
     * @param name non-null
     * @throws IllegalArgumentException if name is null
     */
    public void setName(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        this.name.setValue(name);
    }

    /**
     * Add the {@link CourseEnrollment} to this semester.
     *
     * @param courseEnrollment the enrollment to add to this semester, non-null
     * @throws IllegalArgumentException if the {@link CourseEnrollment#getSemester()} isn't this this semester
     */
    public void addCourseEnrollment(CourseEnrollment courseEnrollment) throws IllegalArgumentException {
        if (courseEnrollment == null) {
            throw new IllegalArgumentException("CourseEnrollment cannot be null.");
        }
        if (!equals(courseEnrollment.getSemester())) {
            throw new IllegalArgumentException(
                    "Cannot add courseEnrollment (" + courseEnrollment + ") with different semester ("
                            + courseEnrollment.getSemester() + ") to semester (" + this + ")");
        }
        courseEnrollmentList.add(courseEnrollment);
    }

    /**
     * Remove the {@link CourseEnrollment} from this semester.
     *
     * @param courseEnrollment the enrollment to remove from this semester, non-null
     * @throws IllegalArgumentException if the {@link CourseEnrollment#getSemester()} isn't this this semester
     */
    public void removeCourseEnrollment(CourseEnrollment courseEnrollment) throws IllegalArgumentException {
        if (courseEnrollment == null) {
            throw new IllegalArgumentException("CourseEnrollment cannot be null.");
        }
        if (!equals(courseEnrollment.getSemester())) {
            throw new IllegalArgumentException(
                    "Cannot remove courseEnrollment (" + courseEnrollment + ") with different semester ("
                            + courseEnrollment.getSemester() + ") from semester (" + this + ")");
        }
        courseEnrollmentList.remove(courseEnrollment);
    }

    public ObservableList<CourseEnrollment> getCourseEnrollmentList() {
        return courseEnrollmentList.get();
    }

    /**
     * The JavaFX property for {@link #getCourseEnrollmentList()}.
     *
     * @return the property of {@link #getCourseEnrollmentList()}
     */
    public ListProperty<CourseEnrollment> courseEnrollmentListProperty() {
        return courseEnrollmentList;
    }

    @Override
    public String toString() {
        return "Semester[" + getName() + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semester)) {
            return false;
        }
        Semester semester = (Semester) o;
        return new EqualsBuilder().append(getName(), semester.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }
}
