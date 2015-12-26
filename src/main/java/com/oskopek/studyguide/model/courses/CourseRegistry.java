package com.oskopek.studyguide.model.courses;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.*;

/**
 * Serves as a cache and/or database for/of {@link Course}s.
 */
public class CourseRegistry {

    private Map<String, Course> courseIdMap;

    /**
     * Create a new empty registry.
     */
    public CourseRegistry() {
        this.courseIdMap = new HashMap<>();
    }

    /**
     * Put (add or replace) a course into the registry.
     *
     * @see Course#equals(Object)
     * @param course non-null
     * @throws IllegalArgumentException if course is null
     */
    public void putCourse(Course course) throws IllegalArgumentException {
        if (course == null) {
            throw new IllegalArgumentException("Cannot add null Course to registry.");
        }
        courseIdMap.put(course.getId(), course);
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

    public Collection<Course> getCourses() {
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
