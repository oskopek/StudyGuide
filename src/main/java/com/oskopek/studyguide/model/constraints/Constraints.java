package com.oskopek.studyguide.model.constraints;

import com.oskopek.studyguide.constraints.Constraint;
import com.oskopek.studyguide.constraints.GlobalConstraint;

import java.util.List;

/**
 * A set of {@link CourseGroup}s and their {@link Constraint}s, along with global {@link Constraint}s.
 */
public class Constraints {

    private List<CourseGroup> courseGroupList;
    private List<GlobalConstraint> globalConstraintList;

}
