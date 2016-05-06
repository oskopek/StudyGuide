package com.oskopek.studyguide.model.constraints;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.List;

/**
 * Defines a group of courses that can be validated against a {@link com.oskopek.studyguide.constraint.Constraint}.
 */
public class CourseGroup {

    private final ListProperty<Course> courseList;

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseGroup() {
        // needed by CDI
        this.courseList = new SimpleListProperty<>();
    }

    /**
     * Builds a new, non-empty course group.
     *
     * @param courseList non-empty list of courses
     * @throws IllegalArgumentException if any parameter is null or any list is empty
     */
    public CourseGroup(Collection<Course> courseList) throws IllegalArgumentException {
        if (courseList == null || courseList.isEmpty()) {
            throw new IllegalArgumentException("The parameters cannot be null and the lists cannot be empty.");
        }
        this.courseList = new SimpleListProperty<>(FXCollections.observableArrayList(courseList));
    }

    /**
     * The list property of courses in this group.
     *
     * @return non-null, not empty
     */
    public ReadOnlyListProperty<Course> courseListProperty() {
        return courseList;
    }

    /**
     * The internal course list.
     *
     * @return non-null list of {@link Course}s
     */
    @JsonGetter
    private ObservableList<Course> getCourseList() {
        return courseList.get();
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param courseList the list of {@link Course}s to set
     */
    private void setCourseList(List<Course> courseList) {
        this.courseList.set(FXCollections.observableArrayList(courseList));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getCourseList())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseGroup)) {
            return false;
        }
        CourseGroup group = (CourseGroup) o;
        return new EqualsBuilder()
                .append(getCourseList(), group.getCourseList())
                .isEquals();
    }

    @Override
    public String toString() {
        return "CourseGroup[" + courseList.size() + ']';
    }
}
