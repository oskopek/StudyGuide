package com.oskopek.studyguide.constraint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;

/**
 * A general contract for all constraints operating on the {@link com.oskopek.studyguide.model.StudyPlan} model.
 */
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({@JsonSubTypes.Type(value = CourseEnrollmentCorequisiteConstraint.class,
        name = "CourseEnrollmentCorequisiteConstraint"),
        @JsonSubTypes.Type(value = CourseEnrollmentPrerequisiteConstraint.class,
                name = "CourseEnrollmentPrerequisiteConstraint"),
        @JsonSubTypes.Type(value = CourseGroupCreditsPercentageConstraint.class,
                name = "CourseGroupCreditsPercentageConstraint"),
        @JsonSubTypes.Type(value = CourseGroupCreditsSumConstraint.class,
                name = "CourseGroupCreditsSumConstraint"),
        @JsonSubTypes.Type(value = CourseGroupFulfilledAllConstraint.class,
                name = "CourseGroupFulfilledAllConstraint"),
        @JsonSubTypes.Type(value = GlobalCourseRepeatedEnrollmentConstraint.class,
                name = "GlobalCourseRepeatedEnrollmentConstraint"),
        @JsonSubTypes.Type(value = GlobalCreditsSumConstraint.class, name = "GlobalCreditsSumConstraint")})
public interface Constraint {

    /**
     * The method should verify if the given constraint was broken, and if so,
     * call the {@link #fireBrokenEvent(String, Course)} with the specific reason.
     */
    void validate();

    /**
     * Used for firing a broken constraint event if the constraint is broken.
     *
     * @param message the reason why the constraint is broken
     * @param changed the course that triggered the constraint breakage
     */
    void fireBrokenEvent(String message, Course changed);

    /**
     * Used for firing a broken constraint event if the constraint is broken.
     *
     * @param message the reason why the constraint is broken
     * @param changed the course enrollment that triggered the constraint breakage
     */
    void fireBrokenEvent(String message, CourseEnrollment changed);

    /**
     * Used for firing a fixed constraint event if the constraint is fixed.
     *
     * @param originallyBroken the originally broken (now fixed) constraint
     */
    void fireFixedEvent(Constraint originallyBroken);
}
