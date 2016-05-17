package com.oskopek.studyguide.constraint;

import com.google.common.eventbus.Subscribe;
import com.oskopek.studyguide.constraint.event.BrokenGlobalConstraintEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Constraint on a global level (on all {@link com.oskopek.studyguide.model.CourseEnrollment}s in the
 * {@link com.oskopek.studyguide.model.StudyPlan}.
 */
public abstract class GlobalConstraint extends DefaultConstraint {

    @Override
    @Subscribe
    public void validate(Course changed) {
        validate();
    }

    @Override
    @Subscribe
    public void validate(CourseEnrollment changed) {
        validate();
    }

    @Override
    public void fireBrokenEvent(String message, Course changed) {
        fireBrokenEvent(message);
    }

    @Override
    public void fireBrokenEvent(String message, CourseEnrollment changed) {
        fireBrokenEvent(message);
    }

    /**
     * The method should verify if the given global constraint was broken, and if so,
     * call {@link #fireBrokenEvent(String)} with the specific reason.
     */
    protected abstract void validate();

    /**
     * Used for firing the {@link BrokenGlobalConstraintEvent} if the constraint is broken.
     *
     * @param message the reason why the constraint is broken
     */
    protected void fireBrokenEvent(String message) {
        eventBus.post(new BrokenGlobalConstraintEvent(message, this));
    }
}
