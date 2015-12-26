package com.oskopek.studyguide.model.constraints;

import com.oskopek.studyguide.constraints.Constraint;
import com.oskopek.studyguide.constraints.GlobalConstraint;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of {@link CourseGroup}s and their {@link Constraint}s, along with global {@link Constraint}s.
 */
public class Constraints {

    private List<CourseGroup> courseGroupList;
    private List<GlobalConstraint> globalConstraintList;

    /**
     * Initialize an empty set of constraints.
     */
    public Constraints() {
        courseGroupList = new ArrayList<>();
        globalConstraintList = new ArrayList<>();
    }

    /**
     * Get the list of course groups (which can have constraints specific to them).
     *
     * @see CourseGroup#getGroupConstraintList()
     * @return non-null
     */
    public List<CourseGroup> getCourseGroupList() {
        return courseGroupList;
    }

    /**
     * Get the list of global constraints.
     *
     * @return non-null
     */
    public List<GlobalConstraint> getGlobalConstraintList() {
        return globalConstraintList;
    }
}
