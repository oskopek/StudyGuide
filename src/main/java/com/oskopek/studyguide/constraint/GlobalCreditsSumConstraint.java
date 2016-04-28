package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.courses.Credits;

/**
 * Checks if the total {@link com.oskopek.studyguide.model.courses.Credits} sum of fulfilled
 * {@link com.oskopek.studyguide.model.courses.Course}s is at least N.
 */
public class GlobalCreditsSumConstraint extends GlobalConstraint {

    private final Credits totalNeeded;

    /**
     * Default constructor.
     *
     * @param totalNeeded the minimum credit amount the student needs to achieve over the course of the study plan
     */
    public GlobalCreditsSumConstraint(Credits totalNeeded) {
        this.totalNeeded = totalNeeded;
    }

    @Override
    public boolean isBroken(SemesterPlan plan) {
        return plan.getSemesterList().stream().flatMap(s -> s.getCourseEnrollmentList().stream())
                .filter(CourseEnrollment::isFulfilled).map(ce -> ce.getCourse().getCredits().getCreditValue())
                .reduce(0, Integer::sum) >= totalNeeded.getCreditValue();
    }

    @Override
    public void fireIfBroken(SemesterPlan plan) {
        // TODO impl
    }
}
