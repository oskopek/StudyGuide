package com.oskopek.studyguide.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * Represents the {@link com.oskopek.studyguide.model.courses.Course} distribution in the {@link StudyPlan}.
 */
public class SemesterPlan {

    private ListProperty<Semester> semesterList;

    /**
     * Create a new empty instance.
     */
    public SemesterPlan() {
        this.semesterList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * TODO: Internal semester list.
     *
     * @return non-null list of {@link Semester}s
     */
    public List<Semester> getSemesterList() {
        return semesterList;
    }

    /**
     * The JavaFX property for {@link #getSemesterList()}.
     *
     * @return the property of {@link #getSemesterList()}
     */
    public ListProperty<Semester> semesterListProperty() {
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
        return new EqualsBuilder().append(getSemesterList(), that.getSemesterList()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getSemesterList()).toHashCode();
    }
}
