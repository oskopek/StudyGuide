package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.constraint.event.BrokenGlobalConstraintEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constraint on a global level (on all {@link com.oskopek.studyguide.model.CourseEnrollment}s in the
 * {@link com.oskopek.studyguide.model.StudyPlan}.
 */
public abstract class GlobalConstraint extends DefaultConstraint {

    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Private default constructor, needed by CDI.
     */
    protected GlobalConstraint() {
        // needed for CDI
    }

    @Override
    public void validate(Course changed) {
        logger.trace("Caught event {} at {}", changed, this);
        validate();
    }

    @Override
    public void validate(CourseEnrollment changed) {
        logger.trace("Caught event {} at {}", changed, this);
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
        eventBus.post(new BrokenGlobalConstraintEvent(messages, message, this));
    }
}
