package com.oskopek.studyguide.model.courses;

/**
 * Types of {@link Course}s in a {@link com.oskopek.studyguide.model.constraints.CourseGroup}
 * with respect to the {@link com.oskopek.studyguide.model.StudyPlan}.
 */
public enum CourseGroupType {

    /**
     * Default (open) course.
     */
    DEFAULT,

    /**
     * Optional course in the study plan.
     */
    OPTIONAL,

    /**
     * Compulsory course in the study plan.
     */
    COMPULSORY

}
