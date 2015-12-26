package com.oskopek.studyguide.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the {@link com.oskopek.studyguide.model.courses.Course} distribution in the {@link StudyPlan}.
 */
public class SemesterPlan {

    private List<Semester> semesterList;

    /**
     * Create a new empty instance.
     */
    public SemesterPlan() {
        this.semesterList = new ArrayList<>();
    }

    /**
     * TODO: Internal semester list.
     *
     * @return non-null list of {@link Semester}s
     */
    public List<Semester> getSemesterList() {
        return semesterList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SemesterPlan)) {
            return false;
        }
        SemesterPlan that = (SemesterPlan) o;
        return new EqualsBuilder().append(semesterList, that.semesterList).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(semesterList).toHashCode();
    }
}
