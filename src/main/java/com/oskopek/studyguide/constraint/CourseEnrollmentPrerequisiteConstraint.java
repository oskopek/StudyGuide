package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.courses.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if all the prerequisite courses for a {@link com.oskopek.studyguide.model.CourseEnrollment}
 * are marked as fulfilled (at the latest one semester before the current one).
 */
public class CourseEnrollmentPrerequisiteConstraint extends CourseEnrollmentConstraint {

    /**
     * Default constructor.
     *
     * @param enrollment the enrollment to check
     */
    public CourseEnrollmentPrerequisiteConstraint(CourseEnrollment enrollment) {
        super(enrollment);
    }

    @Override
    public boolean isBroken(SemesterPlan plan) {
        List<Course> corequisites = new ArrayList<>(getEnrollment().getCourse().getCorequisites());
        int semesterIndex = plan.getSemesterList().indexOf(getEnrollment().getSemester()) - 1;
        if (semesterIndex < 0) {
            return !corequisites.isEmpty();
        }

        List<CourseEnrollment> enrollmentsUntilNow = takeUntilSemester(plan, plan.getSemesterList().get(semesterIndex));
        for (CourseEnrollment enrollment : enrollmentsUntilNow) {
            int found = corequisites.indexOf(enrollment.getCourse());
            if (found >= 0 && enrollment.isFulfilled()) {
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
