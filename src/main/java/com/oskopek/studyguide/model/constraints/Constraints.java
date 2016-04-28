package com.oskopek.studyguide.model.constraints;

import com.oskopek.studyguide.constraint.BrokenConstraintsEvent;
import com.oskopek.studyguide.constraint.Constraint;
import com.oskopek.studyguide.constraint.CourseEnrollmentConstraint;
import com.oskopek.studyguide.constraint.CourseGroupConstraint;
import com.oskopek.studyguide.constraint.GlobalConstraint;
import com.oskopek.studyguide.model.SemesterPlan;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A set of {@link CourseGroup}s and their {@link Constraint}s, along with global constraints.
 */
public class Constraints {

    private ListProperty<CourseGroupConstraint> courseGroupConstraintList;
    private ListProperty<GlobalConstraint> globalConstraintList;
    private ListProperty<CourseEnrollmentConstraint> courseEnrollmentConstraintList;

    @Inject
    private Event<BrokenConstraintsEvent> brokenConstraintsEvent;

    /**
     * Initialize an empty set of constraints.
     */
    public Constraints() {
        courseGroupConstraintList = new SimpleListProperty<>(FXCollections.observableArrayList());
        globalConstraintList = new SimpleListProperty<>(FXCollections.observableArrayList());
        courseEnrollmentConstraintList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * Private copy constructor making an immutable copy.
     *
     * @see BrokenConstraintsEvent
     * @see #recalculate(SemesterPlan, Object)
     * @param courseGroupConstraintList the broken course group constraints
     * @param globalConstraintList the broken global constraints
     * @param courseEnrollmentConstraintList the broken course enrollment constraints
     */
    private Constraints(List<CourseGroupConstraint> courseGroupConstraintList,
                       List<GlobalConstraint> globalConstraintList,
                       List<CourseEnrollmentConstraint> courseEnrollmentConstraintList) {
        this.courseGroupConstraintList = new SimpleListProperty<>(FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(courseGroupConstraintList)));
        this.globalConstraintList = new SimpleListProperty<>(FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(globalConstraintList)));
        this.courseEnrollmentConstraintList = new SimpleListProperty<>(FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(courseEnrollmentConstraintList)));
    }

    /**
     * Recalculates all possible constraint conflicts.
     *
     * @deprecated remove me in favor of a more dynamic approach.
     * @param plan the plan in which to check
     * @param changed the changed value, ignored right now
     * @param <Changed_> the internal type of the changed value
     */
    public <Changed_> void recalculate(SemesterPlan plan, Changed_ changed) {
        // TODO what about displaying broken constraints inline?
        // TODO do not recalculate everything?
        List<CourseGroupConstraint> brokenCourseGroupConstraints
                = courseGroupConstraintList.stream().filter(c -> c.isBroken(plan)).collect(Collectors.toList());
        List<GlobalConstraint> brokenGlobalConstraints
                = globalConstraintList.stream().filter(c -> c.isBroken(plan)).collect(Collectors.toList());
        List<CourseEnrollmentConstraint> brokenCourseEnrollmentConstraints
                = courseEnrollmentConstraintList.stream().filter(c -> c.isBroken(plan)).collect(Collectors.toList());
        brokenConstraintsEvent.fire(new BrokenConstraintsEvent(new Constraints(brokenCourseGroupConstraints,
                brokenGlobalConstraints, brokenCourseEnrollmentConstraints)));
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
     * Get the list property of {@link com.oskopek.studyguide.model.CourseEnrollment} constraints.
     *
     * @return non-null
     */
    public ListProperty<CourseEnrollmentConstraint> courseEnrollmentConstraintListProperty() {
        return courseEnrollmentConstraintList;
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
     * Private setter for Jackson persistence.
     *
     * @param courseGroupConstraintList the list of {@link CourseGroupConstraint}s to set
     */
    private void setCourseGroupConstraintList(List<CourseGroupConstraint> courseGroupConstraintList) {
        this.courseGroupConstraintList.set(FXCollections.observableArrayList(courseGroupConstraintList));
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseEnrollmentConstraintList the list of {@link CourseEnrollmentConstraint}s to set
     */
    private void setCourseEnrollmentConstraintList(List<CourseEnrollmentConstraint> courseEnrollmentConstraintList) {
        this.courseEnrollmentConstraintList.set(FXCollections.observableArrayList(courseEnrollmentConstraintList));
    }
}
