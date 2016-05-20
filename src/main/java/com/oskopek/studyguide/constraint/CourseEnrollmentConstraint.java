package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.constraint.event.BrokenCourseEnrollmentConstraintEvent;
import com.oskopek.studyguide.constraint.event.StringMessageEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Collection;

/**
 * Constraint on the level of individual {@link com.oskopek.studyguide.model.CourseEnrollment}s.
 * Is an abstract parent of constraints for checking course prerequisites and corequisites.
 */
public abstract class CourseEnrollmentConstraint extends DefaultConstraint {

    private CourseEnrollment courseEnrollment;

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseEnrollmentConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param courseEnrollment the course enrollment to check
     */
    public CourseEnrollmentConstraint(CourseEnrollment courseEnrollment) {
        this.courseEnrollment = courseEnrollment;
    }

    /**
     * Get the course enrollment we're checking.
     *
     * @return the course enrollment
     */
    public CourseEnrollment getCourseEnrollment() {
        return courseEnrollment;
    }

    /**
     * Set the course enrollment we're checking.
     *
     * @param courseEnrollment the course enrollment
     */
    public void setCourseEnrollment(CourseEnrollment courseEnrollment) {
        this.courseEnrollment = courseEnrollment;
    }

    @Override
    public void fireBrokenEvent(String reason, Course course) {
        fireBrokenEvent(reason, (CourseEnrollment) null);
    }

    @Override
    public void fireBrokenEvent(String reason, CourseEnrollment enrollment) {
        eventBus.post(new BrokenCourseEnrollmentConstraintEvent(messages, reason, this, enrollment));
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param message the message to broadcast
     * @param brokenRequirements the courses whose requirements were broken
     * @return the String to use as a message, localized
     */
    protected String generateMessage(String message, Collection<Course> brokenRequirements) {
        return messages.getString(message) + StringUtils.join(brokenRequirements.iterator(), ", ");
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCourseEnrollment()).append(getClass().getName()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseEnrollmentConstraint)) {
            return false;
        }
        CourseEnrollmentConstraint that = (CourseEnrollmentConstraint) o;
        return new EqualsBuilder().append(getCourseEnrollment(), that.getCourseEnrollment())
                // used for differentiating types of Course Enrollment constraints
                .append(getClass().getName(), that.getClass().getName()).isEquals();
    }
}
