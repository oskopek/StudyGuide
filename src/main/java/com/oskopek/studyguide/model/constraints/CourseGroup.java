package com.oskopek.studyguide.model.constraints;

import com.oskopek.studyguide.constraints.GroupConstraint;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseGroupType;

import java.util.List;

/**
 * Defines a group of courses that can be validated against a {@link com.oskopek.studyguide.constraints.Constraint}.
 */
public class CourseGroup {

    private CourseGroupType courseGroupType;
    private List<Course> courseList;
    private List<GroupConstraint> groupConstraintList;

}
