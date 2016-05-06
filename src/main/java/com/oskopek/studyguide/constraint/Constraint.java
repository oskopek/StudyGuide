package com.oskopek.studyguide.constraint;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;

import javax.enterprise.event.Observes;

/**
 * A general contract for all constraints operating on the {@link com.oskopek.studyguide.model.StudyPlan} model.
 */
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public interface Constraint {

    /**
     * The method should verify if the given constraint was broken, and if so,
     * call the {@link #fireBrokenEvent(String, Course)} with the specific reason.
     *
     * @param changed the course that triggered the constraint breakage
     */
    void validate(@Observes Course changed);

    /**
     * The method should verify if the given constraint was broken, and if so,
     * call the {@link #fireBrokenEvent(String, CourseEnrollment)} with the specific reason.
     *
     * @param changed the course enrollment that triggered the constraint breakage
     */
    void validate(@Observes CourseEnrollment changed);

    // TODO OPTIONAL rework fire methods

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

}
