package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.courses.Course;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Checks if the number of times a student is enrolled in a given course in a single semester is not more than one.
 */
public class GlobalCourseEnrolledTwiceInASemesterConstraint extends GlobalConstraint {

    private final String message = "constraint.courseEnrolledTwiceInSemester";

    @Override
    public void validate() {
        for (Semester semester : semesterPlan) {
            Map<Course, List<CourseEnrollment>> groupedByCourse = semester.getCourseEnrollmentList().stream()
                    .collect(Collectors.groupingBy(CourseEnrollment::getCourse));
            for (Map.Entry<Course, List<CourseEnrollment>> entry : groupedByCourse.entrySet()) {
                if (entry.getValue().size() > 1) {
                    Course course = entry.getKey();
                    fireBrokenEvent(generateMessage(semester, course));
                }
            }
        }
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param semester the semester in which the constraint was broken
     * @param course the course that was enrolled twice in the given semester
     * @return the String to use as a message, localized
     */
    private String generateMessage(Semester semester, Course course) {
        return String.format(messages.getString(message), course.getName(), semester.getName());
    }
}
