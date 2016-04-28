package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.constraints.CourseGroup;

/**
 * Constraint on the level of individual {@link com.oskopek.studyguide.model.constraints.CourseGroup}s.
 */
public abstract class CourseGroupConstraint implements Constraint {

    private CourseGroup courseGroup;

    /**
     * Default constructor.
     *
     * @param courseGroup the referenced course group
     */
    public CourseGroupConstraint(CourseGroup courseGroup) {
        this.courseGroup = courseGroup;
    }

    /**
     * Get the checked course group.
     *
     * @return the course group
     */
    public CourseGroup getCourseGroup() {
        return courseGroup;
    }
}
