package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.SemesterPlan;

import javax.inject.Inject;
import java.util.ResourceBundle;

public abstract class DefaultConstraint implements Constraint {

    @Inject
    protected SemesterPlan semesterPlan;

    @Inject
    protected ResourceBundle messages;

}
