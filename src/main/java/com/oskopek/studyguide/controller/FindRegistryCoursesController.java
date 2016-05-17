package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Finds course being searched for in the {@link CourseRegistry}.
 */
public class FindRegistryCoursesController implements FindCourses {

    private static final FloatCoursePairComparator<Map.Entry<Float, ? extends Course>> floatCoursePairComparator =
            new FloatCoursePairComparator<>();
    private static final StringMetric metric =
            StringMetricBuilder.with(new CosineSimilarity<>()).simplify(Simplifiers.toLowerCase())
                    .simplify(Simplifiers.removeDiacritics()).tokenize(Tokenizers.qGram(1)).build();
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
     * The {@link StringMetric} used to calculate string (id, name, ...) similarities.
     *
     * @return the non-null metric
     */
    protected static StringMetric getMetric() {
        return metric;
    }

    /**
     * Computes a similarity index for all course  to the {@code key}, sorts the courses according to it and returns
     * the first 10 (most similar).
     *
     * @param key the key to search
     * @return the 10 most similar courses to the key
     * @see org.simmetrics.StringMetric
     * @see CosineSimilarity
     */
    @Override
    public Stream<Course> findCourses(String key) {
        // limit id search artificially to prevent weird results (a user searches names, mostly)
        Stream<? extends Map.Entry<Float, ? extends Course>> sortedById =
                mapToSortedPairs(course -> metric.compare(key, course.getId())).limit(3);
        Stream<? extends Map.Entry<Float, ? extends Course>> sortedByName =
                mapToSortedPairs(course -> metric.compare(key, course.nameOrLocalizedName())).limit(10);
        return Stream.concat(sortedById, sortedByName).sorted(floatCoursePairComparator).distinct().limit(10)
                .map(Map.Entry::getValue);
    }

    @Override
    public Stream<Course> findCoursesById(String id) {
        return findCoursesInternal(course -> metric.compare(id, course.getId()));
    }

    @Override
    public Stream<Course> findCoursesByName(String name, Locale locale) {
        return findCoursesInternal(course -> metric.compare(name, course.nameOrLocalizedName()));
    }

    /**
     * Returns a stream of courses, using the search function on all {@link FindCourses} instances.
     *
     * @param valueFunction the function to compute a float, based on which courses are sorted
     * @return a stream of courses
     * @see #mapToSortedPairs(Function)
     */
    private Stream<Course> findCoursesInternal(Function<? super Course, Float> valueFunction) {
        return mapToSortedPairs(valueFunction).map(Map.Entry::getValue);
    }

    /**
     * Returns a stream of sorted float-course pairs, using the search function on all {@link FindCourses} instances.
     *
     * @param valueFunction the function to compute a float, based on which courses are sorted
     * @return a stream of sorted float-course pairs
     */
    private Stream<? extends Map.Entry<Float, ? extends Course>> mapToSortedPairs(
            Function<? super Course, Float> valueFunction) {
        return courseRegistry.courseMapValues().parallelStream()
                .map(course -> new HashMap.SimpleEntry<>(valueFunction.apply(course), course))
                .sorted(floatCoursePairComparator);
    }

    /**
     * A Float-Course pair comparator: first compare according to the float, if equal,
     * compare according to {@link Course#getName()}s lexicographically.
     *
     * @param <T> an implementation of {@link java.util.Map.Entry}
     */
    private static class FloatCoursePairComparator<T extends Map.Entry<Float, ? extends Course>>
            implements Comparator<T> {

        @Override
        public int compare(T a, T b) {
            int res = Float.compare(b.getKey(), a.getKey());
            return res != 0 ? res : a.getValue().getName().compareTo(b.getValue().getName());
        }
    }
}
