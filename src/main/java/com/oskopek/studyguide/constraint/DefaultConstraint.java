package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.SemesterPlan;

import javax.inject.Inject;

public abstract class DefaultConstraint implements Constraint {

    @Inject
    protected SemesterPlan semesterPlan;

}
