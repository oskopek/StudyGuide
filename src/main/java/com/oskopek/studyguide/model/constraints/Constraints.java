package com.oskopek.studyguide.model.constraints;

import com.oskopek.studyguide.constraint.Constraint;
import com.oskopek.studyguide.constraint.GlobalConstraint;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * A set of {@link CourseGroup}s and their {@link Constraint}s, along with global constraints.
 */
public class Constraints {

    private ListProperty<CourseGroup> courseGroupList;
    private ListProperty<GlobalConstraint> globalConstraintList;

    /**
     * Initialize an empty set of constraints.
     */
    public Constraints() {
        courseGroupList = new SimpleListProperty<>(FXCollections.observableArrayList());
        globalConstraintList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * Get the list property of course groups (which can have constraints specific to them).
     *
     * @return non-null
     * @see CourseGroup#courseGroupConstraintListProperty()
     */
    public ListProperty<CourseGroup> courseGroupListProperty() {
        return courseGroupList;
    }

    /**
     * Get the list property of global constraint.
     *
     * @return non-null
     */
    public ListProperty<GlobalConstraint> globalConstraintListProperty() {
        return globalConstraintList;
    }

    /**
     * The internal global constraint list.
     *
     * @return non-null list of {@link GlobalConstraint}s
     */
    public ObservableList<GlobalConstraint> getGlobalConstraintList() {
        return globalConstraintList.get();
    }

    /**
     * The internal course group list.
     *
     * @return non-null list of {@link CourseGroup}s
     */
    public ObservableList<CourseGroup> getCourseGroupList() {
        return courseGroupList.get();
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseGroupList the list of {@link CourseGroup}s to set
     */
    private void setCourseGroupList(List<CourseGroup> courseGroupList) {
        this.courseGroupList.set(FXCollections.observableArrayList(courseGroupList));
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param globalConstraintList the list of {@link GlobalConstraint}s to set
     */
    private void setGlobalConstraintList(List<GlobalConstraint> globalConstraintList) {
        this.globalConstraintList.set(FXCollections.observableArrayList(globalConstraintList));
    }
}
