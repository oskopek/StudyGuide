package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.SemesterPlan;

import javax.inject.Inject;
import java.util.ResourceBundle;

/**
 * An CDI-enabling abstraction over all constraints. Enables them to use the given {@link SemesterPlan} and
 * a {@link ResourceBundle} for translating messages displayed to the user.
 */
public abstract class DefaultConstraint implements Constraint {

    @Inject
    protected SemesterPlan semesterPlan;

    @Inject
    protected transient ResourceBundle messages;

}
