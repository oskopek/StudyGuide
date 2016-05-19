package com.oskopek.studyguide.constraint.event;

import com.oskopek.studyguide.constraint.Constraint;
import com.oskopek.studyguide.model.CourseEnrollment;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ResourceBundle;

/**
 * The event used for reporting broken course group constraints.
 */
public class BrokenCourseEnrollmentConstraintEvent extends StringMessageEvent {

    private CourseEnrollment enrollment;

    /**
     * Default constructor.
     *
     * @param message the message to use as a reason why the constraint is broken
     * @param broken the constraint that was broken and generated this event
     * @param enrollment the course enrollment that the constraint broke on
     */
    public BrokenCourseEnrollmentConstraintEvent(ResourceBundle messages, String message, Constraint broken,
                                                 CourseEnrollment enrollment) {
        super(messages, message, broken);
        this.enrollment = enrollment;
    }

    /**
     * Gets the course enrollment that the constraint broke on.
     *
     * @return the course enrollment
     */
    public CourseEnrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String message() {
        return messages.getString("constraint.courseEnrollmentInvalid") + " " + getMessage();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getEnrollment()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BrokenCourseEnrollmentConstraintEvent)) {
            return false;
        }
        BrokenCourseEnrollmentConstraintEvent that = (BrokenCourseEnrollmentConstraintEvent) o;
        return new EqualsBuilder().appendSuper(super.equals(o)).append(getEnrollment(), that.getEnrollment())
                .isEquals();
    }
}
