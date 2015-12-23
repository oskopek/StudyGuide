package com.oskopek.studyguide.model;

import com.oskopek.studyguide.constraints.CourseEnrollmentConstraint;
import com.oskopek.studyguide.model.courses.Course;

import java.util.List;

/**
 * An instance (enrollment) of a {@link Course} in a given {@link Semester}.
 */
public class CourseEnrollment {

    private Course course;
    private Semester semester;
    private boolean fulfilled;
    private List<CourseEnrollmentConstraint> courseEnrollmentConstraintList;

}
