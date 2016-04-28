package com.oskopek.studyguide.model;

import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * Single semester in a {@link StudyPlan}.
 */
public class Semester {

    private final StringProperty name;
    private final ListProperty<CourseEnrollment> courseEnrollmentList;

    /**
     * Private constructor for Jackson persistence.
     */
    private Semester() {
        name = new SimpleStringProperty();
        this.courseEnrollmentList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * Create an empty semester.
     *
     * @param name unique, non-null
     * @throws IllegalArgumentException if name is null
     */
    public Semester(String name) throws IllegalArgumentException {
        this();
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        this.name.setValue(name);
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
     * Generates and adds a {@link CourseEnrollment} to this semester.
     *
     * @param course the course to add to this semester, non-null
     * @throws IllegalArgumentException if the {@link Course} is null or if {@link Course} is already enrolled
     */
    public void addCourseEnrollment(Course course) throws IllegalArgumentException {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null.");
        }
        addCourseEnrollment(new CourseEnrollment(course, this, false));
    }

    /**
     * Add the {@link CourseEnrollment} to this semester.
     *
     * @param courseEnrollment the enrollment to add to this semester, non-null
     * @throws IllegalArgumentException if the {@link Course} is already enrolled
     */
    private void addCourseEnrollment(CourseEnrollment courseEnrollment) throws IllegalArgumentException {
        if (courseEnrollment == null) {
            throw new IllegalArgumentException("CourseEnrollment cannot be null.");
        }
        if (courseEnrollmentList.contains(courseEnrollment)) {
            throw new IllegalArgumentException("Semester (" + this + ") already contains course enrollment of course ("
                    + courseEnrollment.getCourse() + ").");
        }
        courseEnrollmentList.add(courseEnrollment);
    }

    /**
     * Remove the {@link CourseEnrollment} from this semester.
     *
     * @param courseEnrollment the enrollment to remove from this semester, non-null
     * @throws IllegalArgumentException if the course enrollment is null
     */
    public void removeCourseEnrollment(CourseEnrollment courseEnrollment) throws IllegalArgumentException {
        if (courseEnrollment == null) {
            throw new IllegalArgumentException("CourseEnrollment cannot be null.");
        }
        courseEnrollmentList.remove(courseEnrollment);
    }

    /**
     * The {@link CourseEnrollment}s regarding this semester.
     *
     * @return non-null, possibly empty
     */
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

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseEnrollmentList the list of {@link CourseEnrollment}s to set
     */
    public void setCourseEnrollmentList(List<CourseEnrollment> courseEnrollmentList) {
        this.courseEnrollmentList.set(FXCollections.observableArrayList(courseEnrollmentList));
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
