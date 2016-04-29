package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BrokenCourseEnrollmentConstraintEvent extends StringMessageEvent {

    private CourseEnrollment enrollment;

    public BrokenCourseEnrollmentConstraintEvent(String message, CourseEnrollment enrollment) {
        super(message);
        this.enrollment = enrollment;
    }

    public CourseEnrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String message() {
        return "%constraint.courseenrollmentinvalid" + getMessage();
    }

    @Override
    public String toString() {
        return "BrokenCEConstraintEvent[" + enrollment + ", " + getMessage() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof BrokenCourseEnrollmentConstraintEvent)) return false;

        BrokenCourseEnrollmentConstraintEvent that = (BrokenCourseEnrollmentConstraintEvent) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getEnrollment(), that.getEnrollment())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getEnrollment())
                .toHashCode();
    }
}
