package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;

import java.util.Locale;
import java.util.stream.Stream;

/**
 * Defines an interface for finding courses based on a string search key.
 */
public interface FindCourses {

    /**
     * Search for courses in a implementation-specific way.
     * This method is optional.
     * @param key the key to look for (implementation-specific)
     * @return a non-null stream of {@link Course}s that match best (implementation-specific)
     */
    default Stream<Course> findCourses(String key) {
        throw new UnsupportedOperationException("This method is not implemented.");
    }

    /**
     * Search for courses corresponding to the given name in the data-source.
     *
     * @param id the course id
     * @return a non-null stream of {@link Course}s that match best
     */
    Stream<Course> findCoursesById(String id);

    /**
     * Search for courses corresponding to the given id in the data-source.
     *
     * @param name the course name
     * @param locale the locale in which to search the names ({@link Course#getLocalizedName()}).
     * @return a non-null stream of {@link Course}s that match best
     */
    Stream<Course> findCoursesByName(String name, Locale locale);

}
