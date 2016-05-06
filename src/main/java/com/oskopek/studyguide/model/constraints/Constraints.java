package com.oskopek.studyguide.model.constraints;

import com.oskopek.studyguide.constraint.Constraint;
import com.oskopek.studyguide.constraint.CourseEnrollmentConstraint;
import com.oskopek.studyguide.constraint.CourseGroupConstraint;
import com.oskopek.studyguide.constraint.GlobalConstraint;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * A set of {@link CourseGroup}s and their {@link Constraint}s, along with global constraints.
 */
public class Constraints {

    private ListProperty<CourseGroupConstraint> courseGroupConstraintList;
    private ListProperty<GlobalConstraint> globalConstraintList;
    private ListProperty<CourseEnrollmentConstraint> courseEnrollmentConstraintList;

    /**
     * Initialize an empty set of constraints.
     */
    public Constraints() {
        courseGroupConstraintList = new SimpleListProperty<>(FXCollections.observableArrayList());
        globalConstraintList = new SimpleListProperty<>(FXCollections.observableArrayList());
        courseEnrollmentConstraintList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * Get the list of {@link CourseGroup} constraints.
     *
     * @return non-null
     */
    public ObservableList<CourseGroupConstraint> getCourseGroupConstraintList() {
        return courseGroupConstraintList.get();
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseGroupConstraintList the list of {@link CourseGroupConstraint}s to set
     */
    private void setCourseGroupConstraintList(List<CourseGroupConstraint> courseGroupConstraintList) {
        this.courseGroupConstraintList.set(FXCollections.observableArrayList(courseGroupConstraintList));
    }

    /**
     * Get the list property of {@link CourseGroup} constraints.
     *
     * @return non-null
     */
    public ListProperty<CourseGroupConstraint> courseGroupConstraintListProperty() {
        return courseGroupConstraintList;
    }

    /**
     * Get the list of global constraints.
     *
     * @return non-null
     */
    public ObservableList<GlobalConstraint> getGlobalConstraintList() {
        return globalConstraintList.get();
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param globalConstraintList the list of {@link GlobalConstraint}s to set
     */
    private void setGlobalConstraintList(List<GlobalConstraint> globalConstraintList) {
        this.globalConstraintList.set(FXCollections.observableArrayList(globalConstraintList));
    }

    /**
     * Get the list property of global constraints.
     *
     * @return non-null
     */
    public ListProperty<GlobalConstraint> globalConstraintListProperty() {
        return globalConstraintList;
    }

    /**
     * Get the list of {@link com.oskopek.studyguide.model.CourseEnrollment} constraints.
     *
     * @return non-null
     */
    public ObservableList<CourseEnrollmentConstraint> getCourseEnrollmentConstraintList() {
        return courseEnrollmentConstraintList.get();
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseEnrollmentConstraintList the list of {@link CourseEnrollmentConstraint}s to set
     */
    private void setCourseEnrollmentConstraintList(List<CourseEnrollmentConstraint> courseEnrollmentConstraintList) {
        this.courseEnrollmentConstraintList.set(FXCollections.observableArrayList(courseEnrollmentConstraintList));
    }

    /**
     * Get the list property of {@link com.oskopek.studyguide.model.CourseEnrollment} constraints.
     *
     * @return non-null
     */
    public ListProperty<CourseEnrollmentConstraint> courseEnrollmentConstraintListProperty() {
        return courseEnrollmentConstraintList;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getCourseGroupConstraintList())
                .append(getGlobalConstraintList())
                .append(getCourseEnrollmentConstraintList())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Constraints)) {
            return false;
        }
        Constraints that = (Constraints) o;
        return new EqualsBuilder()
                .append(getCourseGroupConstraintList(), that.getCourseGroupConstraintList())
                .append(getGlobalConstraintList(), that.getGlobalConstraintList())
                .append(getCourseEnrollmentConstraintList(), that.getCourseEnrollmentConstraintList())
                .isEquals();
    }
}
