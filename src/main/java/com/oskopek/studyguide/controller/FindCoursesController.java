package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.view.FindCoursePane;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Controller for searching courses in multiple data-sources.
 */
public class FindCoursesController extends AbstractController<FindCoursePane> implements FindCourses {

    private List<FindCourses> findCoursesList;

    /**
     * Creates an empty instance.
     */
    public FindCoursesController() {
        this.findCoursesList = new ArrayList<>();
    }

    /**
     * Add a {@link FindCourses} instance to the list of course data-sources.
     *
     * @param findCourses a non-null instance
     */
    public void addFindCourses(FindCourses findCourses) {
        findCoursesList.add(findCourses);
    }

    /**
     * Search for courses corresponding to the given key in all {@link FindCourses} data-sources.
     *
     * @param key the key to search for (id, name, ...)
     * @param locale the locale in which to search the names ({@link Course#getLocalizedName()}).
     * @return a non-null, five element list of {@link Course}s that match best
     */
    public List<Course> findCourses(String key, Locale locale) { // TODO search for ids and names
        return findCoursesList.parallelStream().map((f) -> f.findCourses(key, locale))
                .flatMap(l -> l.stream()).distinct().limit(5).collect(Collectors.toList());
    }

}
