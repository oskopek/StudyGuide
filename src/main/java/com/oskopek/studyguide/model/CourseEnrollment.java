package com.oskopek.studyguide.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oskopek.studyguide.constraint.BrokenCourseEnrollmentConstraintEvent;
import com.oskopek.studyguide.constraint.BrokenResetEvent;
import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;

/**
 * An instance (enrollment) of a {@link Course} in a given {@link Semester}.
 */
public class CourseEnrollment extends ObservableValueBase<CourseEnrollment>
        implements ObservableValue<CourseEnrollment> {

    private final ObjectProperty<Course> course;
    private final BooleanProperty fulfilled;
    @JsonBackReference("semester-courseenrollment")
    private final ObjectProperty<Semester> semester;
    private transient ObjectProperty<BrokenCourseEnrollmentConstraintEvent> brokenConstraint
            = new SimpleObjectProperty<>(); // TODO PRIORITY move this somewhere

    private transient Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Private constructor for Jackson persistence.
     */
    private CourseEnrollment() {
        course = new SimpleObjectProperty<>();
        fulfilled = new SimpleBooleanProperty();
        semester = new SimpleObjectProperty<>();
        registerChangeEventListeners();
    }

    /**
     * Create a basic instance of an enrollment.
     *
     * @param course the enrolled course, non-null
     * @param semester the semester the course is enrolled in
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
        registerChangeEventListeners();
    }

    /**
     * Creates a shallow copy of the given CourseEnrollment. Used for events.
     *
     * @param original the course enrollment to copy
     * @return a new CourseEnrollment copy
     * @see #fireValueChangedEvent()
     */
    public static CourseEnrollment copy(CourseEnrollment original) {
        Course course = Course.copy(original.getCourse());
        boolean fulfilled = original.fulfilled.get();
        Semester semester = original.getSemester();
        return new CourseEnrollment(course, semester, fulfilled);
    }

    /**
     * Handler for constraint fixed events.
     *
     * @param event the observed event
     */
    private void onFixedConstraint(@Observes BrokenResetEvent event) {
        if (brokenConstraint.get() != null
                && brokenConstraint.get().getBrokenConstraint().equals(event.getOriginallyBroken())) {
            brokenConstraint.set(null);
        }
    }

    /**
     * Handler for constraint broken events.
     *
     * @param event the observed event
     */
    private void onBrokenConstraint(@Observes BrokenCourseEnrollmentConstraintEvent event) {
        if (equals(event.getEnrollment())) {
            brokenConstraint.set(event);
        }
    }

    /**
     * The property indicating where this enrollment is breaking a constraint.
     * @return the property
     */
    public ObjectProperty<BrokenCourseEnrollmentConstraintEvent> brokenConstraintProperty() {
        return brokenConstraint;
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
     * Private setter for the course the student is enrolled in (for Jackson).
     *
     * @param course the course to set
     */
    private void setCourse(Course course) {
        this.course.set(course);
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
     * Private setter for the semester the student is enrolled in the course (for Jackson).
     *
     * @param semester the semester to set
     */
    private void setSemester(Semester semester) {
        this.semester.set(semester);
    }

    /**
     * The JavaFX property for {@link #getSemester()}.
     *
     * @return the property of {@link #getSemester()}
     */
    public ObjectProperty<Semester> semesterProperty() {
        return semester;
    }

    @JsonIgnore
    @Override
    public CourseEnrollment getValue() {
        return CourseEnrollment.copy(this);
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
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(course).append(semester).toHashCode();
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
    public String toString() {
        return "CourseEnrollment[" + course.get() + ", " + semester.get() + ']';
    }
}
