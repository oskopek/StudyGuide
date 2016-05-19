package com.oskopek.studyguide.constraint;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.model.SemesterPlan;

import javax.inject.Inject;
import java.util.ResourceBundle;

/**
 * An CDI-enabling abstraction over all constraints. Enables them to use the given {@link SemesterPlan} and
 * a {@link ResourceBundle} for translating messages displayed to the user.
 */
public abstract class DefaultConstraint implements Constraint {

    @Inject
    protected transient SemesterPlan semesterPlan;

    @Inject
    @JacksonInject
    protected transient ResourceBundle messages;

    @Inject
    @JacksonInject
    protected transient EventBus eventBus;

    @JsonIgnore
    public void setSemesterPlan(SemesterPlan semesterPlan) {
        this.semesterPlan = semesterPlan;
    }
}
