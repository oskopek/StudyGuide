package com.oskopek.studyguide.constraint;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.constraint.event.FixedConstraintEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.SemesterPlan;

import javax.inject.Inject;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * An CDI-enabling abstraction over all constraints. Enables them to use the given {@link SemesterPlan} and
 * a {@link ResourceBundle} for translating messages displayed to the user.
 */
public abstract class DefaultConstraint implements Constraint {

    protected transient SemesterPlan semesterPlan;

    @Inject
    @JacksonInject
    protected transient ResourceBundle messages;

    @Inject
    @JacksonInject
    protected transient EventBus eventBus;

    /**
     * Utility method: takes semesters from the plan and while they are sooner in the plan than the given semester
     * collects all their {@link CourseEnrollment}s in a stream. Simulates Haskell's takeWhile method.
     *
     * @param plan the plan from which to take semesters
     * @param semester the semester to stop collecting at (still included in the stream)
     * @return the collected course enrollment stream
     */
    protected static Stream<CourseEnrollment> takeUntilSemester(SemesterPlan plan, Semester semester) {
        Stream<CourseEnrollment> enrollments = Stream.empty();
        for (Semester pSemester : plan) {
            enrollments = Stream.concat(enrollments, pSemester.getCourseEnrollmentList().stream());
            if (semester.equals(pSemester)) {
                break;
            }
        }
        return enrollments;
    }

    /**
     * Utility method: takes semesters from the plan and while they are sooner in the plan than the given semester
     * collects all their {@link CourseEnrollment}s in a stream. Simulates Haskell's take method.
     *
     * @param plan the plan from which to take semesters
     * @param semester the semester to stop collecting at (still included in the stream), counted from 0
     * @return the collected course enrollment stream
     */
    protected static Stream<CourseEnrollment> takeUntilSemester(SemesterPlan plan, int semester) {
        if (semester < 1) {
            return Stream.empty();
        }
        if (semester > plan.getSemesterList().size()) {
            semester = plan.getSemesterList().size();
        }
        return takeUntilSemester(plan, plan.getSemesterList().get(semester - 1));
    }

    @Override
    public void fireFixedEvent(Constraint originallyBroken) {
        eventBus.post(new FixedConstraintEvent(originallyBroken));
    }

    /**
     * Get the semester plan associated with this constraint. Should be the semester plan this constraint is in.
     *
     * @return the semester plan
     */
    public SemesterPlan getSemesterPlan() {
        return semesterPlan;
    }

    /**
     * Get the semester plan associated with this constraint. Should be the semester plan this constraint is in,
     * but is not enforced.
     *
     * @param semesterPlan the semester plan to set
     */
    @JsonIgnore
    public void setSemesterPlan(SemesterPlan semesterPlan) {
        this.semesterPlan = semesterPlan;
    }
}
