package com.oskopek.studyguide.model;

import com.oskopek.studyguide.model.constraints.Constraints;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Default implementation of a {@link StudyPlan}.
 */
public class DefaultStudyPlan implements StudyPlan {

    private ObjectProperty<SemesterPlan> semesterPlan;
    private ObjectProperty<Constraints> constraints;
    private ObjectProperty<CourseRegistry> courseRegistry;

    /**
     * Create an empty instance of a study plan.
     */
    public DefaultStudyPlan() {
        this.constraints = new SimpleObjectProperty<>(new Constraints());
        this.courseRegistry = new SimpleObjectProperty<>(new CourseRegistry());
        this.semesterPlan = new SimpleObjectProperty<>(new SemesterPlan());
    }

    /**
     * The {@link com.oskopek.studyguide.constraints.Constraint}s placed on this plan.
     *
     * @return may be null
     */
    public Constraints getConstraints() {
        return constraints.get();
    }

    /**
     * A registry of available {@link com.oskopek.studyguide.model.courses.Course}s.
     * Not necessarily the only source of courses.
     *
     * @return may be null
     */
    public CourseRegistry getCourseRegistry() {
        return courseRegistry.get();
    }

    /**
     * An enumeration of individual {@link Semester}s.
     *
     * @return may be null
     */
    public SemesterPlan getSemesterPlan() {
        return semesterPlan.get();
    }


    /**
     * The JavaFX property for {@link #getSemesterPlan()}.
     *
     * @return the property of {@link #getSemesterPlan()}
     */
    public ObjectProperty<SemesterPlan> semesterPlanProperty() {
        return semesterPlan;
    }

    /**
     * The JavaFX property for {@link #getConstraints()}.
     *
     * @return the property of {@link #getConstraints()}
     */
    public ObjectProperty<Constraints> constraintsProperty() {
        return constraints;
    }

    /**
     * The JavaFX property for {@link #getCourseRegistry()}.
     *
     * @return the property of {@link #getCourseRegistry()}
     */
    public ObjectProperty<CourseRegistry> courseRegistryProperty() {
        return courseRegistry;
    }

    @Override
    public String toString() {
        return "DefaultStudyPlan[" + getSemesterPlan() + ']';
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

        return new EqualsBuilder().append(getSemesterPlan(), that.getSemesterPlan())
                .append(getConstraints(), that.getConstraints()).append(getCourseRegistry(), that.getCourseRegistry())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getSemesterPlan()).append(getConstraints())
                .append(getCourseRegistry()).toHashCode();
    }
}
