package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.constraint.event.BrokenCourseGroupConstraintEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.constraints.CourseGroup;
import com.oskopek.studyguide.model.courses.Course;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constraint on the level of individual {@link com.oskopek.studyguide.model.constraints.CourseGroup}s.
 */
public abstract class CourseGroupConstraint extends DefaultConstraint {

    private final transient Logger logger = LoggerFactory.getLogger(getClass());
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
    } // TODO remove these?

    /**
     * Get the checked course group.
     *
     * @return the course group
     */
    public CourseGroup getCourseGroup() {
        return courseGroup;
    }

    /**
     * Set the checked course group.
     *
     * @param courseGroup the group to set
     */
    public void setCourseGroup(CourseGroup courseGroup) {
        this.courseGroup = courseGroup;
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
        eventBus.post(new BrokenCourseGroupConstraintEvent(messages, message, this, courseGroup));
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
        return "CourseGroupConstraint[" + (courseGroup == null ? "null" : courseGroup.courseListProperty().size())
                + ']';
    }
}
