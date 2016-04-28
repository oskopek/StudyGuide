package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.courses.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if all the corequisite courses for a {@link com.oskopek.studyguide.model.CourseEnrollment}
 * are enrolled in (at the latest this current semester).
 */
public class CourseEnrollmentCorequisiteConstraint extends CourseEnrollmentConstraint {

    /**
     * Default constructor.
     *
     * @param enrollment the enrollment to check
     */
    public CourseEnrollmentCorequisiteConstraint(CourseEnrollment enrollment) {
        super(enrollment);
    }

    @Override
    public boolean isBroken(SemesterPlan plan) {
        List<CourseEnrollment> enrollmentsUntilNow = takeUntilSemester(plan, getEnrollment().getSemester());
        List<Course> corequisites = new ArrayList<>(getEnrollment().getCourse().getCorequisites());
        for (CourseEnrollment enrollment : enrollmentsUntilNow) {
            int found = corequisites.indexOf(enrollment.getCourse());
            if (found >= 0) {
                corequisites.remove(found);
            }
        }
        return !corequisites.isEmpty();
    }

    @Override
    public void fireIfBroken(SemesterPlan plan) {
        // TODO impl
    }
}
