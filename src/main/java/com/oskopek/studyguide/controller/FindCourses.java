package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;

import java.util.List;
import java.util.Locale;

/**
 * Defines an interface for finding courses based on a string search key.
 */
public interface FindCourses {

    /**
     *
     * @param key
     * @param locale
     * @return
     */
    List<Course> findCourses(String key, Locale locale);

}
