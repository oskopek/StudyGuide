package com.oskopek.studyguide.constraint;

import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.model.SemesterPlan;
import org.slf4j.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ResourceBundle;

/**
 * An CDI-enabling abstraction over all constraints. Enables them to use the given {@link SemesterPlan} and
 * a {@link ResourceBundle} for translating messages displayed to the user.
 */
public abstract class DefaultConstraint implements Constraint {

    @Inject
    protected transient Instance<SemesterPlan> semesterPlan;

    @Inject
    protected transient Instance<ResourceBundle> messages;

    @Inject
    protected transient Instance<EventBus> eventBus;

    @Inject
    protected transient Instance<Logger> logger;

}
