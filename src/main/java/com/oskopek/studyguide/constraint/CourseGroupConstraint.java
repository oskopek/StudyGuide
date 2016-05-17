package com.oskopek.studyguide.constraint;

import com.google.common.eventbus.Subscribe;
import com.oskopek.studyguide.constraint.event.BrokenCourseGroupConstraintEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.constraints.CourseGroup;
import com.oskopek.studyguide.model.courses.Course;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Constraint on the level of individual {@link com.oskopek.studyguide.model.constraints.CourseGroup}s.
 */
public abstract class CourseGroupConstraint extends DefaultConstraint {

    private CourseGroup courseGroup;

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseGroupConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param courseGroup the referenced course group
     */
    public CourseGroupConstraint(CourseGroup courseGroup) {
        this.courseGroup = courseGroup;
    }

    /**
     * Get the checked course group.
     *
     * @return the course group
     */
    public CourseGroup getCourseGroup() {
        return courseGroup;
    }

    /**
     * The method should verify if the given constraint was broken, and if so,
     * call the {@link #fireBrokenEvent(String)} with the specific reason.
     */
    protected abstract void validate();

    @Override
    @Subscribe
    public void validate(Course changed) {
        logger.get().trace("Caught event {} at {}", changed, this);
        if (courseGroup.courseListProperty().contains(changed)) {
            validate();
        }
    }

    @Override
    @Subscribe
    public void validate(CourseEnrollment changed) {
        logger.get().trace("Caught event {} at {}", changed, this);
        validate(changed.getCourse());
    }

    @Override
    public void fireBrokenEvent(String message, Course changed) {
        fireBrokenEvent(message);
    }

    @Override
    public void fireBrokenEvent(String message, CourseEnrollment changed) {
        fireBrokenEvent(message);
    }

    /**
     * Used for firing the {@link BrokenCourseGroupConstraintEvent} if the constraint is broken.
     *
     * @param message the reason why the constraint is broken
     */
    public void fireBrokenEvent(String message) {
        eventBus.get().post(new BrokenCourseGroupConstraintEvent(message, this, courseGroup));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCourseGroup()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseGroupConstraint)) {
            return false;
        }
        CourseGroupConstraint that = (CourseGroupConstraint) o;
        return new EqualsBuilder().append(getCourseGroup(), that.getCourseGroup()).isEquals();
    }

    @Override
    public String toString() {
        return "CourseGroupConstraint[" + courseGroup.courseListProperty().size() + ']';
    }
}
