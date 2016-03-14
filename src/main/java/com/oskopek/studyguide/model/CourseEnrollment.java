package com.oskopek.studyguide.model;

import com.oskopek.studyguide.constraints.CourseEnrollmentConstraint;
import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * An instance (enrollment) of a {@link Course} in a given {@link Semester}.
 */
public class CourseEnrollment {

    private Course course;
    private Semester semester;
    private BooleanProperty fulfilled;
    private List<CourseEnrollmentConstraint> courseEnrollmentConstraintList;

    private StringProperty fulfilledStringWrapper;

    /**
     * Create a basic instance of an enrollment.
     *
     * @param course    the enrolled course, non-null
     * @param semester  the semester student enrolled course in, non-null
     * @param fulfilled true iff the student passed
     * @throws IllegalArgumentException if course or semester are null
     */
    public CourseEnrollment(Course course, Semester semester, boolean fulfilled) throws IllegalArgumentException {
        if (course == null || semester == null) {
            throw new IllegalArgumentException("Course or semester is null");
        }
        this.course = course;
        this.semester = semester;
        this.fulfilled = new SimpleBooleanProperty(fulfilled);
        this.courseEnrollmentConstraintList = new ArrayList<>();
        this.fulfilledStringWrapper = new SimpleStringProperty();
        this.fulfilled.addListener((observable, oldValue, newValue) -> {
            this.fulfilledStringWrapper.setValue(newValue ? "✓" : "✗"); // TODO This belongs into the gui, not model!
        });
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
     * The semester that the student enrolled into this course.
     *
     * @return non-null
     */
    public Semester getSemester() {
        return semester;
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

    /**
     * Converts the {@link #fulfilledProperty()} into a string representation (tick vs. cross).
     * @param fulfilled the boolean value to convert
     * @return a tick if true, a cross if false
     * @deprecated Should be removed in favor of a button and handler
     */
    @Deprecated
    private String convertFullfiledToString(boolean fulfilled) { // TODO this belongs into the gui, not model!
        return fulfilled ? "v" : "X";
    }

    /**
     * A wrapper of {@link #isFulfilled()}.
     * @see #convertFullfiledToString(boolean)
     * @return a property wrapper of {@link #isFulfilled()}
     * @deprecated Should be removed in favor of a button and handler
     */
    @Deprecated
    public StringProperty fulfilledPropertyStringWrapper() {
        return fulfilledStringWrapper;
    }

    @Override
    public String toString() {
        return "CourseEnr[" + course + '@' + semester + ']';
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
        return new EqualsBuilder().append(course, that.course).append(semester, that.semester).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(course).append(semester).toHashCode();
    }
}
