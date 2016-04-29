package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;

import javax.enterprise.event.Observes;

/**
 * A general contract for all constraints operating on the {@link com.oskopek.studyguide.model.StudyPlan} model.
 */
public interface Constraint {

    void validate(@Observes Course changed);

    void validate(@Observes CourseEnrollment changed);

    // TODO rework fire methods

    void fireBrokenEvent(String message, Course changed);

    void fireBrokenEvent(String message, CourseEnrollment changed);

}
