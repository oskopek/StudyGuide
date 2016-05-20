package com.oskopek.studyguide.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents the {@link com.oskopek.studyguide.model.courses.Course} distribution in the {@link StudyPlan}.
 */
public class SemesterPlan implements Iterable<Semester> {

    private final ListProperty<Semester> semesterList;

    /**
     * Create a new empty instance.
     */
    public SemesterPlan() {
        this.semesterList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    /**
     * The internal semester list.
     *
     * @return non-null list of {@link Semester}s
     */
    public List<Semester> getSemesterList() {
        return semesterList;
    }

    /**
     * Private setter for Jackson persistence.
     *
     * @param semesterList the list of {@link Semester}s to set
     */
    private void setSemesterList(List<Semester> semesterList) {
        this.semesterList.set(FXCollections.observableArrayList(semesterList));
    }

    /**
     * Remove the {@link Semester} from the semester list.
     *
     * @param semester the semester to be removed
     * @see javafx.collections.ObservableList#remove(Object)
     */
    public void removeSemester(Semester semester) {
        semesterList.remove(semester);
    }

    /**
     * Add the {@link Semester} to the semester list.
     *
     * @param semester the semester to be added
     * @see javafx.collections.ObservableList#add(Object)
     */
    public boolean addSemester(Semester semester) {
        if (findSemester(semester.getName()).isPresent()) {
            return false;
        } else {
            semesterList.add(semester);
            return true;
        }
    }

    /**
     * Find the semester with the given name in this plan. Note, semester names are unique.
     *
     * @param name the name to look for
     * @return an optional semester (will be present if we found a semester with the given name in this plan)
     */
    public Optional<Semester> findSemester(String name) {
        return semesterList.stream().filter(s -> s.getName().equals(name)).findAny();
    }

    /**
     * The JavaFX property for {@link #getSemesterList()}.
     *
     * @return the property of {@link #getSemesterList()}
     */
    public ListProperty<Semester> semesterListProperty() {
        return semesterList;
    }

    /**
     * Returns the last semester in the list of semesters.
     *
     * @return the last semester, null if the list is empty
     * @see #getSemesterList()
     */
    public Semester lastSemester() {
        if (semesterList.isEmpty()) {
            return null;
        }
        return semesterList.get(semesterList.size() - 1);
    }

    /**
     * Concatenates all course enrollments from all semesters into one stream.
     *
     * @return a serial stream of all course enrollments in the plan
     */
    public Stream<CourseEnrollment> allCourseEnrollments() {
        return getSemesterList().stream().flatMap(ce -> ce.getCourseEnrollmentList().stream());
    }

    @Override
    public Iterator<Semester> iterator() {
        return semesterList.iterator();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getSemesterList()).toHashCode();
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
    public String toString() {
        return "SemesterPlan[" + semesterList.size() + ']';
    }
}
