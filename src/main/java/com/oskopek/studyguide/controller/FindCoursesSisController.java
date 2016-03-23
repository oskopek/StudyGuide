package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;

import java.util.Locale;
import java.util.stream.Stream;

/**
 * Finds course being searched for in an <a href="https://www.cuni.cz/UK-4428.html">IS Studium</a> (SIS) instance.
 */
public class FindCoursesSisController implements FindCourses {

    @Override
    public Stream<Course> findCourses(String key) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Stream<Course> findCoursesById(String id) {
        throw new UnsupportedOperationException("Not implemented yet.");    }

    @Override
    public Stream<Course> findCoursesByName(String name, Locale locale) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
