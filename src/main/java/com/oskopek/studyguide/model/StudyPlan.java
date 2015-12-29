package com.oskopek.studyguide.model;

/**
 * Representation of the whole study plan model.
 */
public interface StudyPlan {

    // TODO

    /**
     * Get the corresponding {@link SemesterPlan} instance.
     *
     * @return a non-null semester plan instance
     */
    SemesterPlan getSemesterPlan();

}
