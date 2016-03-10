package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Finds course being searched for in the {@link CourseRegistry}.
 */
public class FindRegistryCoursesController extends ChooseCourseController implements FindCourses {

    private CourseRegistry courseRegistry;

    private StringMetric metric = StringMetricBuilder.with(new CosineSimilarity<>()).simplify(Simplifiers.toLowerCase())
            .simplify(Simplifiers.removeDiacritics()).tokenize(Tokenizers.whitespace()).build();

    /**
     * Create a controller instance for a {@link CourseRegistry}.
     *
     * @param courseRegistry non-null back-end for the controller
     */
    public FindRegistryCoursesController(CourseRegistry courseRegistry) {
        this.courseRegistry = courseRegistry;
    }

    /**
     * Computes a similarity index for all course  to the {@code key}, sorts the courses according to it and returns
     * the first 10 (most similar).
     *
     * @param key    the key to search
     * @return the 10 most similar courses to the key
     * @see org.simmetrics.StringMetric
     * @see CosineSimilarity
     */
    @Override
    public Stream<Course> findCourses(String key) {
        Stream<? extends Map.Entry<Float, ? extends Course>> sortedById =
                mapToSortedPairs(course -> metric.compare(course.getId(), key)).limit(10);
        Stream<? extends Map.Entry<Float, ? extends Course>> sortedByName =
                mapToSortedPairs(course -> metric.compare(course.name(Locale.getDefault()), key)).limit(10);
        return Stream.concat(sortedById, sortedByName)
                .sorted((a, b) -> Float.compare(a.getKey(), b.getKey())).distinct()
                .limit(10).map(Map.Entry::getValue);
    }

    @Override
    public Stream<Course> findCoursesById(String id) {
        return findCoursesInternal(course -> metric.compare(course.getId(), id));
    }

    @Override
    public Stream<Course> findCoursesByName(String name, Locale locale) {
        return findCoursesInternal(course -> metric.compare(course.name(locale), name));
    }

    /**
     * Returns a stream of courses, using the search function on all {@link FindCourses} instances.
     * @param valueFunction the function to compute a float, based on which courses are sorted
     * @return
     */
    private Stream<Course> findCoursesInternal(Function<? super Course, Float> valueFunction) {
        return mapToSortedPairs(valueFunction).map(Map.Entry::getValue);
    }

    /**
     * Returns a stream of sorted float-course pairs, using the search function on all {@link FindCourses} instances.
     * @param valueFunction the function to compute a float, based on which courses are sorted
     * @return
     */
    private Stream<? extends Map.Entry<Float, ? extends Course>> mapToSortedPairs(Function<? super Course, Float> valueFunction) {
        return courseRegistry.courseMapValues().parallelStream()
                .map(course -> new HashMap.SimpleEntry<>(valueFunction.apply(course), course))
                .sorted((a, b) -> Float.compare(a.getKey(), b.getKey()));
    }
}
