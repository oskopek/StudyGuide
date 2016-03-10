package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Finds course being searched for in an <a href="https://www.cuni.cz/UK-4428.html">IS Studium</a> (SIS) instance.
 */
public class FindCoursesSisController extends ChooseCourseController implements FindCourses {

    @Override
    public Stream<Course> findCourses(String key) {
        return null;
    }

    @Override
    public Stream<Course> findCoursesById(String id) {
        return null;
    }

    @Override
    public Stream<Course> findCoursesByName(String name, Locale locale) {
        return null;
    }
}
