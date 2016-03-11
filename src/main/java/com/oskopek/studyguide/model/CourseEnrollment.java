package com.oskopek.studyguide.model;

import com.oskopek.studyguide.constraints.CourseEnrollmentConstraint;
import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
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

    private StringBinding fulfilledWrapper;

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
        this.fulfilledWrapper = Bindings.createStringBinding(() -> convertFullfiledToString(fulfilled), fulfilledProperty());
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

    public BooleanProperty fulfilledProperty() {
        return fulfilled;
    }

    private String convertFullfiledToString(boolean fulfilled) {
        return fulfilled ? "v" : "X"; // TODO chars
    }

    public StringProperty fulfilledPropertyWrapper() {
        return fulfilledWrapper.;
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
