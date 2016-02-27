package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Finds course being searched for in an <a href="https://www.cuni.cz/UK-4428.html">IS Studium</a> (SIS) instance.
 */
public class FindCoursesSisController extends ChooseCourseController implements FindCourses {

    @Override
    public List<Course> findCourses(String key, Locale locale) {
        return new ArrayList<>(); // TODO
    }
}
