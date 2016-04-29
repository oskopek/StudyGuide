package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.courses.Course;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GlobalCourseEnrollmentTwiceInASemesterConstraint extends GlobalConstraint {

    public GlobalCourseEnrollmentTwiceInASemesterConstraint() {
        // intentionally empty
    }

    @Override
    public boolean isBroken(SemesterPlan plan) {
        boolean broken = false;
        for (Semester semester : plan) {
            Map<Course, List<CourseEnrollment>> groupedByCourse = semester.getCourseEnrollmentList().stream()
                    .collect(Collectors.groupingBy(ce -> ce.getCourse()));
            for (Map.Entry<Course, List<CourseEnrollment>> entry : groupedByCourse.entrySet()) {
                if (entry.getValue().size() > 1) {
                    broken = true;
                    // TODO fire an event here
                }
            }
        }
        return broken;
    }

    @Override
    public void fireIfBroken(SemesterPlan plan) {
        // TODO impl
    }
}
