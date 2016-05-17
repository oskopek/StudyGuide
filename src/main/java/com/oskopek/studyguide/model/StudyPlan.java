package com.oskopek.studyguide.model;

import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.model.constraints.Constraints;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import com.oskopek.studyguide.weld.EventBusTranslator;

/**
 * Representation of the whole study plan model.
 */
public interface StudyPlan {

    /**
     * The {@link com.oskopek.studyguide.constraint.Constraint}s placed on this plan.
     *
     * @return may be null
     */
    Constraints getConstraints();

    /**
     * A registry of available {@link com.oskopek.studyguide.model.courses.Course}s.
     * Not necessarily the only source of courses.
     *
     * @return may be null
     */
    CourseRegistry getCourseRegistry();

    /**
     * Get the corresponding {@link SemesterPlan} instance.
     *
     * @return a non-null semester plan instance
     */
    SemesterPlan getSemesterPlan();

    StudyPlan register(EventBus eventBus, EventBusTranslator eventBusTranslator);

    StudyPlan unregister(EventBus eventBus, EventBusTranslator eventBusTranslator);

}
