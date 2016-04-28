package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.SemesterPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Constraint on the level of individual {@link com.oskopek.studyguide.model.CourseEnrollment}s.
 * Is an abstract parent of constraints for checking course prerequisites and corequisites.
 */
public abstract class CourseEnrollmentConstraint extends GlobalConstraint {

    private CourseEnrollment enrollment;

    /**
     * Default constructor.
     *
     * @param enrollment the enrollment to check
     */
    public CourseEnrollmentConstraint(CourseEnrollment enrollment) {
        this.enrollment = enrollment;
    }

    /**
     * Get the enrollment we're checking.
     *
     * @return the enrollment
     */
    public CourseEnrollment getEnrollment() {
        return enrollment;
    }

    /**
     * Utility method: takes semesters from the plan and while they are sooner in the plan than the given semester
     * collects all their {@link CourseEnrollment}s in a list. Simulates Haskell's takeWhile method.
     *
     * @param plan the plan from which to take semesters
     * @param semester the semester to stop collecting at (still included in the list)
     * @return the collected list
     */
    protected static List<CourseEnrollment> takeUntilSemester(SemesterPlan plan, Semester semester) {
        List<CourseEnrollment> enrollments = new ArrayList<>();
        for (Semester pSemester : plan) {
            enrollments.addAll(pSemester.getCourseEnrollmentList());
            if (semester.equals(pSemester)) {
                break;
            }
        }
        return enrollments;
    }
}
