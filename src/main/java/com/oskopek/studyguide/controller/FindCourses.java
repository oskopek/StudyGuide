package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;

import java.util.List;
import java.util.Locale;

/**
 * Defines an interface for finding courses based on a string search key.
 */
public interface FindCourses {

    /**
     * Search for courses corresponding to the given key in the data-source.
     *
     * @param key the key to search for (id, name, ...)
     * @param locale the locale in which to search the names ({@link Course#getLocalizedName()}).
     * @return a non-null, five element list of {@link Course}s that match best
     */
    List<Course> findCourses(String key, Locale locale);

}
