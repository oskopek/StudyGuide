package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.courses.Course;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Checks if the number of times a student is enrolled in a course is less than or equal to N.
 */
public class GlobalCourseRepeatedEnrollmentConstraint extends GlobalConstraint {

    private int maxRepeatedEnrollment;

    /**
     * Default course.
     *
     * @param maxRepeatedEnrollment the maximum count of course enrollments per course
     */
    public GlobalCourseRepeatedEnrollmentConstraint(int maxRepeatedEnrollment) {
        this.maxRepeatedEnrollment = maxRepeatedEnrollment;
    }

    @Override
    public boolean isBroken(SemesterPlan plan) {
        Map<Course, List<CourseEnrollment>> groupByCourse = plan.getSemesterList().stream()
                .flatMap(s -> s.getCourseEnrollmentList().stream())
                .collect(Collectors.groupingBy(ce -> ce.getCourse()));
        for (List<CourseEnrollment> enrollments : groupByCourse.values()) {
            if (enrollments.size() > maxRepeatedEnrollment) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void fireIfBroken(SemesterPlan plan) {
        // TODO impl
    }
}
