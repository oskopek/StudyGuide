package com.oskopek.studyguide.model;

import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValueBase;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An instance (enrollment) of a {@link Course} in a given {@link Semester}.
 */
public class CourseEnrollment extends ObservableValueBase<CourseEnrollment> {

    private final ObjectProperty<Course> course;
    private final BooleanProperty fulfilled;
    private final ObjectProperty<Semester> semester;

    /**
     * Private constructor for Jackson persistence.
     */
    private CourseEnrollment() {
        course = new SimpleObjectProperty<>();
        fulfilled = new SimpleBooleanProperty();
        semester = new SimpleObjectProperty<>();
    }

    /**
     * Create a basic instance of an enrollment.
     *
     * @param course    the enrolled course, non-null
     * @param semester  the semester the course is enrolled in
     * @param fulfilled true iff the student passed
     * @throws IllegalArgumentException if course or semester are null
     */
    public CourseEnrollment(Course course, Semester semester, boolean fulfilled) throws IllegalArgumentException {
        if (course == null) {
            throw new IllegalArgumentException("Course is null");
        }
        this.course = new SimpleObjectProperty<>(course);
        this.fulfilled = new SimpleBooleanProperty(fulfilled);
        this.semester = new SimpleObjectProperty<>(semester);
    }

    /**
     * The course that the student enrolled in.
     *
     * @return non-null
     */
    public Course getCourse() {
        return course.get();
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
     * The JavaFX property for {@link #getCourse()}.
     *
     * @return the property of {@link #getCourse()}
     */
    public ObjectProperty<Course> courseProperty() {
        return course;
    }

    /**
     * The semester that the student enrolled in the course.
     *
     * @return non-null
     */
    public Semester getSemester() {
        return semester.get();
    }
    /**
     * The JavaFX property for {@link #getSemester()}.
     *
     * @return the property of {@link #getSemester()}
     */
    public ObjectProperty<Semester> semesterProperty() {
        return semester;
    }

    @Override
    public CourseEnrollment getValue() {
        return CourseEnrollment.copy(this);
    }

    /**
     * Creates a shallow copy of the given CourseEnrollment. Used for events.
     *
     * @see #fireValueChangedEvent()
     * @param original the course enrollment to copy
     * @return a new CourseEnrollment copy
     */
    public static CourseEnrollment copy(CourseEnrollment original) {
        Course course = Course.copy(original.getCourse());
        boolean fulfilled = original.fulfilled.get();
        Semester semester = original.getSemester();
        return new CourseEnrollment(course, semester, fulfilled);
    }

    /**
     * Register {@link javafx.beans.value.ChangeListener}s to important attributes and notify of a course enrollment
     * change using {@link #fireValueChangedEvent()}.
     */
    private void registerChangeEventListeners() {
        course.addListener((x, y, z) -> fireValueChangedEvent());
        fulfilled.addListener((x, y, z) -> fireValueChangedEvent());
        semester.addListener((x, y, z) -> fireValueChangedEvent());
    }

    @Override
    public String toString() {
        return "CourseEnr[" + course.get() + ", " + semester.get() + ']';
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
