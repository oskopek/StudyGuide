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

    /**
     * Builds a new, non-empty course group.
     *
     * @param courseGroupType     the type of courses in this course
     * @param courseList          non-empty list of courses
     * @param groupConstraintList non-empty list of constraints
     * @throws IllegalArgumentException if any parameter is null or any list is empty
     */
    public CourseGroup(CourseGroupType courseGroupType, List<Course> courseList,
                       List<GroupConstraint> groupConstraintList) throws IllegalArgumentException {
        if (courseGroupType == null || courseList == null || groupConstraintList == null || courseList.isEmpty()
                || groupConstraintList.isEmpty()) {
            throw new IllegalArgumentException("The parameters cannot be null and the lists cannot be empty.");
        }
        this.courseGroupType = courseGroupType;
        this.courseList = courseList;
        this.groupConstraintList = groupConstraintList;
    }

    /**
     * The type of courses in this course group.
     *
     * @return non-null
     */
    public CourseGroupType getCourseGroupType() {
        return courseGroupType;
    }

    /**
     * The courses in this group.
     *
     * @return non-null, not empty
     */
    public List<Course> getCourseList() {
        return courseList;
    }

    /**
     * The constraints imposed on this group.
     *
     * @return non-null, not empty
     */
    public List<GroupConstraint> getGroupConstraintList() {
        return groupConstraintList;
    }
}
