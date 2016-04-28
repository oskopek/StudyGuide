package com.oskopek.studyguide.model;

import com.oskopek.studyguide.constraint.CourseEnrollmentConstraint;
import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An instance (enrollment) of a {@link Course} in a given {@link Semester}.
 */
public class CourseEnrollment {

    private Course course;
    private BooleanProperty fulfilled;
    private ListProperty<CourseEnrollmentConstraint> courseEnrollmentConstraintList;

    /**
     * Private constructor for Jackson persistence.
     */
    private CourseEnrollment() {
        fulfilled = new SimpleBooleanProperty();
        courseEnrollmentConstraintList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * Create a basic instance of an enrollment.
     *
     * @param course    the enrolled course, non-null
     * @param fulfilled true iff the student passed
     * @throws IllegalArgumentException if course or semester are null
     */
    public CourseEnrollment(Course course, boolean fulfilled) throws IllegalArgumentException {
        this();
        if (course == null) {
            throw new IllegalArgumentException("Course s null");
        }
        this.course = course;
        this.fulfilled.setValue(fulfilled);
    }

    /**
     * The course that the student enrolled in.
     *
     * @return non-null
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Is this course marked as passed?
     *
     * @return true if passed, else not passed
     */
    public boolean isFulfilled() {
        return fulfilled.get();
    }

    /**
     * Mark this course as passed/not passed.
     *
     * @param fulfilled true iff passed
     */
    public void setFulfilled(boolean fulfilled) {
        this.fulfilled.setValue(fulfilled);
    }

    /**
     * The JavaFX property for {@link #isFulfilled()}.
     *
     * @return the property of {@link #isFulfilled()}
     */
    public BooleanProperty fulfilledProperty() {
        return fulfilled;
    }

    @Override
    public String toString() {
        return "CourseEnr[" + course + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseEnrollment)) {
            return false;
        }
        CourseEnrollment that = (CourseEnrollment) o;
        return new EqualsBuilder().append(course, that.course).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(course).toHashCode();
    }
}
