package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Checks if the number of times a student is enrolled in a course is less than or equal to N.
 */
public class GlobalCourseRepeatedEnrollmentConstraint extends GlobalConstraint {

    private int maxRepeatedEnrollment;
    private String message = "%constraint.globalcourserepeatedenrollmmentinvalid";

    /**
     * Default course.
     *
     * @param maxRepeatedEnrollment the maximum count of course enrollments per course
     */
    public GlobalCourseRepeatedEnrollmentConstraint(int maxRepeatedEnrollment) {
        this.maxRepeatedEnrollment = maxRepeatedEnrollment;
    }

    @Override
    public void validate() {
        Map<Course, List<CourseEnrollment>> groupByCourse = semesterPlan.getSemesterList().stream()
                .flatMap(s -> s.getCourseEnrollmentList().stream())
                .collect(Collectors.groupingBy(ce -> ce.getCourse()));
        for (Map.Entry<Course, List<CourseEnrollment>> entry : groupByCourse.entrySet()) {
            Course course = entry.getKey();
            List<CourseEnrollment> enrollments = entry.getValue();
            if (enrollments.size() > maxRepeatedEnrollment) {
                fireBrokenEvent(generateMessage(message, enrollments.size(), maxRepeatedEnrollment, course));
            }
        }
    }

    private static String generateMessage(String message, int enrolledTimes, int maxRepeatedEnrollment, Course course) {
        return String.format(message, enrolledTimes, maxRepeatedEnrollment, course.getName()); // TODO expand message first
    }
}
