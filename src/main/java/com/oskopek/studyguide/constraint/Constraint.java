package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.SemesterPlan;

/**
 * A general contract for all constraints operating on the {@link com.oskopek.studyguide.model.StudyPlan} model.
 */
public interface Constraint {

    /**
     * The checking method for the constraint.
     *
     * @param plan the {@link SemesterPlan} to check
     * @return true iff the constraint is broken given the specific plan
     */
    boolean isBroken(SemesterPlan plan);

    /**
     * Call {@link #isBroken(SemesterPlan)} and fire an appropriate event if applicable. This is the method you want to
     * be calling for checking constraints.
     *
     * @param plan the {@link SemesterPlan} to check
     */
    void fireIfBroken(SemesterPlan plan); // TODO reevaluate this

}
