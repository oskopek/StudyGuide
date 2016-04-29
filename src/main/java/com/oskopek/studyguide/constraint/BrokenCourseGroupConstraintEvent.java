package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.constraints.CourseGroup;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BrokenCourseGroupConstraintEvent extends StringMessageEvent {

    private CourseGroup courseGroup;

    public BrokenCourseGroupConstraintEvent(String message, CourseGroup courseGroup) {
        super(message);
        this.courseGroup = courseGroup;
    }

    public CourseGroup getCourseGroup() {
        return courseGroup;
    }

    @Override
    public String message() {
        return messages.getString("constraint.courseGroupInvalid") + getMessage();
    }

    @Override
    public String toString() {
        return "BrokenCGConstraintEvent[" + courseGroup + ", " + getMessage() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof BrokenCourseGroupConstraintEvent)) return false;

        BrokenCourseGroupConstraintEvent that = (BrokenCourseGroupConstraintEvent) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getCourseGroup(), that.getCourseGroup())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getCourseGroup())
                .toHashCode();
    }
}
