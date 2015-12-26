package com.oskopek.studyguide.model;

import com.oskopek.studyguide.model.constraints.Constraints;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Default implementation of a {@link StudyPlan}.
 */
public class DefaultStudyPlan implements StudyPlan {

    private SemesterPlan semesterPlan;
    private Constraints constraints;
    private CourseRegistry courseRegistry;

    /**
     * Create an empty instance of a study plan.
     */
    public DefaultStudyPlan() {
        this.constraints = new Constraints();
        this.courseRegistry = new CourseRegistry();
        this.semesterPlan = new SemesterPlan();
    }

    /**
     * The {@link com.oskopek.studyguide.constraints.Constraint}s placed on this plan.
     *
     * @return may be null
     */
    public Constraints getConstraints() {
        return constraints;
    }

    /**
     * A registry of available {@link com.oskopek.studyguide.model.courses.Course}s.
     * Not necessarily the only source of courses.
     *
     * @return may be null
     */
    public CourseRegistry getCourseRegistry() {
        return courseRegistry;
    }

    /**
     * An enumeration of individual {@link Semester}s.
     *
     * @return may be null
     */
    public SemesterPlan getSemesterPlan() {
        return semesterPlan;
    }

    @Override
    public String toString() {
        return "DefaultStudyPlan[" + semesterPlan + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultStudyPlan)) {
            return false;
        }
        DefaultStudyPlan that = (DefaultStudyPlan) o;
        return new EqualsBuilder().append(semesterPlan, that.semesterPlan).append(constraints, that.constraints)
                .append(courseRegistry, that.courseRegistry).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(semesterPlan).append(constraints).append(courseRegistry).toHashCode();
    }
}
