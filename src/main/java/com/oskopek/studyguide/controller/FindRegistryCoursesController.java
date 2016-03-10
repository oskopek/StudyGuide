package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Finds course being searched for in the {@link CourseRegistry}.
 */
public class FindRegistryCoursesController extends ChooseCourseController implements FindCourses {

    private CourseRegistry courseRegistry;

    /**
     * Create a controller instance for a {@link CourseRegistry}.
     *
     * @param courseRegistry non-null back-end for the controller
     */
    public FindRegistryCoursesController(CourseRegistry courseRegistry) {
        this.courseRegistry = courseRegistry;
    }

    /**
     * Computes a similarity index for all courses to the {@code key}, sorts the courses according to it and returns
     * the first 5 (most similar).
     *
     * @param key    the key to search
     * @param locale the locale in which to search courses
     * @return the 5 most similar courses to the key
     * @see org.simmetrics.StringMetric
     * @see CosineSimilarity
     */
    @Override
    public List<Course> findCourses(String key, Locale locale) {
        return courseRegistry.courseMapValues().parallelStream().map(((Course course) -> new HashMap.SimpleEntry<>(
                StringMetricBuilder.with(new CosineSimilarity<>()).simplify(Simplifiers.toLowerCase())
                        .simplify(Simplifiers.removeDiacritics()).tokenize(Tokenizers.whitespace()).build()
                        .compare(course.name(locale), key), course)))
                .sorted((a, b) -> Float.compare(a.getKey(), b.getKey())).limit(5).map(AbstractMap.SimpleEntry::getValue)
                .collect(Collectors.toList());
    }
}
