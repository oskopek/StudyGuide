package com.oskopek.studyguide.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.oskopek.studyguide.model.constraints.Constraints;
import com.oskopek.studyguide.model.courses.CourseRegistry;

/**
 * Representation of the whole study plan model.
 */
@JsonPropertyOrder({"courseRegistry", "semesterPlan", "constraints"})
public interface StudyPlan {

    /**
     * A registry of available {@link com.oskopek.studyguide.model.courses.Course}s.
     * Not necessarily the only source of courses.
     *
     * @return may be null
     */
    CourseRegistry getCourseRegistry();

    /**
     * Get the corresponding {@link SemesterPlan} instance.
     *
     * @return a non-null semester plan instance
     */
    SemesterPlan getSemesterPlan();

    /**
     * The {@link com.oskopek.studyguide.constraint.Constraint}s placed on this plan.
     *
     * @return may be null
     */
    Constraints getConstraints();


}
