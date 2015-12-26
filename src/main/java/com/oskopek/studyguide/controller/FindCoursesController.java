package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.view.FindCoursePane;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 *
 */
public class FindCoursesController extends AbstractController<FindCoursePane> implements FindCourses {

    private List<FindCoursesController> findCoursesControllerList;

    public FindCoursesController() {
        this.findCoursesControllerList = new ArrayList<>();
    }

    /**
     *
     * @param key
     * @param locale
     * @return
     */
    public List<Course> findCourses(String key, Locale locale) {
        return findCoursesControllerList.parallelStream()
                .map((f) -> f.findCourses(key, locale))
                .flatMap(l -> l.stream())
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

}
