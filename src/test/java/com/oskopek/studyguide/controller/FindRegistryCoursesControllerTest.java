package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.CourseGenerator;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.junit.Before;
import org.junit.Test;
import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by skopeko on 10.3.16.
 */
public class FindRegistryCoursesControllerTest {

    private CourseRegistry registry;
    private FindRegistryCoursesController findRegistryCoursesController;

    @Before
    public void setUp() throws Exception {
        registry = new CourseRegistry();
        for (int i = 0; i < 100; i++) {
            registry.putCourse(CourseGenerator.generateRandomCourse());
        }
        findRegistryCoursesController = new FindRegistryCoursesController(registry);
    }

    @Test
    public void testFindCourses() throws Exception {
        List<Course> courses = findRegistryCoursesController.findCourses("aaaaaaa").collect(Collectors.toList());
        assertNotNull(courses);
        assertEquals(10, courses.size());
        System.out.println(courses);
    }

    @Test
    public void simmetricsTest() throws Exception {
        StringMetric metric = StringMetricBuilder.with(new CosineSimilarity<>()).simplify(Simplifiers.toLowerCase())
                .simplify(Simplifiers.removeDiacritics()).tokenize(Tokenizers.whitespace()).build();
        String[] strings = { "aaa", "aab", "aba", "bab", "dad" };
        String key = "aaa";
        for (String s : strings) {
            System.out.println(metric.compare(key, s) + " : " + key + " vs. " + s);
        }

    }

    @Test
    public void testFindCoursesById() throws Exception {
        fail();
    }

    @Test
    public void testFindCoursesByName() throws Exception {
        fail();
    }
}