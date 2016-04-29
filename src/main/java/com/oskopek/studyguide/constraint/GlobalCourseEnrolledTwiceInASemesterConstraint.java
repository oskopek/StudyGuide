package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.courses.Course;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GlobalCourseEnrolledTwiceInASemesterConstraint extends GlobalConstraint {

    private final static String message = "%constraint.courseenrolledtwiceinsemester"; // TODO expand

    public GlobalCourseEnrolledTwiceInASemesterConstraint() {
        // intentionally empty
    }

    @Override
    public void validate() {
        for (Semester semester : semesterPlan) {
            Map<Course, List<CourseEnrollment>> groupedByCourse = semester.getCourseEnrollmentList().stream()
                    .collect(Collectors.groupingBy(ce -> ce.getCourse()));
            for (Map.Entry<Course, List<CourseEnrollment>> entry : groupedByCourse.entrySet()) {
                if (entry.getValue().size() > 1) {
                    Course course = entry.getKey();
                    fireBrokenEvent(generateMessage(semester, course));
                }
            }
        }
    }

    private static String generateMessage(Semester semester, Course course) {
        // TODO expand message first
        return String.format(message, course.getName(), semester.getName());
    }
}
