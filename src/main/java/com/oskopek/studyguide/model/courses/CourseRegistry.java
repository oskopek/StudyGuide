package com.oskopek.studyguide.model.courses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Serves as a cache and/or database for/of {@link Course}s.
 */
public class CourseRegistry {

    @JsonProperty("courses")
    private Map<String, Course> courseIdMap;

    /**
     * Create a new empty registry.
     */
    public CourseRegistry() {
        this.courseIdMap = new HashMap<>();
    }

    /**
     * Put (add or replace) a course and all it's dependencies into the registry.
     *
     * @param course non-null
     * @throws IllegalArgumentException if course is null
     * @see Course#equals(Object)
     */
    public void putCourse(Course course) throws IllegalArgumentException {
        if (course == null) {
            throw new IllegalArgumentException("Cannot add null Course to registry.");
        }
        courseIdMap.put(course.getId(), course);
        putAllCourses(course.getPrerequisites());
        putAllCourses(course.getCorequisites());
    }

    /**
     * Put (add or replace) a course and without it's dependencies into the registry.
     *
     * @param course non-null
     * @throws IllegalArgumentException if course is null
     * @see Course#equals(Object)
     */
    public void putCourseSimple(Course course) throws IllegalArgumentException {
        if (course == null) {
            throw new IllegalArgumentException("Cannot add null Course to registry.");
        }
        courseIdMap.put(course.getId(), course);
    }

    /**
     * Copies (with overwriting) all courses from the registry into this registry.
     * @param registry the registry to copy courses from
     */
    public void putAllCourses(CourseRegistry registry) {
        putAllCourses(registry.courseMapValues());
    }

    /**
     * Copies (with overwriting) all courses from the iterable into this registry.
     * @param courses the iterable to copy courses from
     */
    public void putAllCourses(Iterable<Course> courses) {
        for (Course course : courses) {
            putCourse(course);
        }
    }

    /**
     * Get a course from the registry.
     *
     * @param id non-null
     * @return null if such a course doesn't exist
     * @throws IllegalArgumentException if id is null
     */
    public Course getCourse(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Cannot get course with null id from registry.");
        }
        return courseIdMap.get(id);
    }

    /**
     * Get all the {@link Course}s in the registry.
     *
     * @return a non-null (may be empty) collection of courses
     */
    public Collection<Course> courseMapValues() {
        return courseIdMap.values();
    }

    @Override
    public String toString() {
        return "CourseRegistry[" + courseIdMap.size() + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseRegistry)) {
            return false;
        }
        CourseRegistry that = (CourseRegistry) o;
        return new EqualsBuilder().append(courseIdMap, that.courseIdMap).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(courseIdMap).toHashCode();
    }
}
