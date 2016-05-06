package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.CourseGenerator;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.junit.Before;
import org.junit.Test;
import org.simmetrics.StringMetric;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit test for {@link FindRegistryCoursesController}.
 */
public class FindRegistryCoursesControllerTest {

    private CourseRegistry registry;
    private FindRegistryCoursesController findRegistryCoursesController;
    private StringMetric metric;

    @Before
    public void setUp() throws Exception {
        registry = new CourseRegistry();
        for (int i = 0; i < 100; i++) {
            registry.putCourse(CourseGenerator.generateRandomCourse());
        }
        findRegistryCoursesController = new FindRegistryCoursesController(registry);
        metric = FindRegistryCoursesController.getMetric();
    }

    @Test
    public void testFindCourses() throws Exception {
        String key = "aaaaaaa";
        List<Course> courses = findRegistryCoursesController.findCourses(key).collect(Collectors.toList());

        List<Course> courseId = findRegistryCoursesController.findCoursesById(key)
                .limit(10).collect(Collectors.toList());
        List<Course> courseName = findRegistryCoursesController.findCoursesByName(key, Locale.getDefault())
                .limit(10).collect(Collectors.toList());

        assertNotNull(courses);
        assertEquals(10, courses.size());
        int index = 0;
        for (Course c : courses) {
            if (courseName.contains(c)) {
                courseName.remove(c);
            } else if (courseId.contains(c)) {
                courseId.remove(c);
            } else {
                fail("The course at " + index  + " is out of order: " + c);
            }
            index++;
        }
    }

    @Test
    public void simmetricsTest() throws Exception {
        String[] strings = {"aba", "bab", "aaa", "dad", "aab"};
        String[] stringsSorted = {"aaa", "aba", "aab", "bab", "dad"};
        String key = "aaa";
        Arrays.sort(strings, (a, b) -> Float.compare(metric.compare(key, b), metric.compare(key, a)));
        assertArrayEquals(stringsSorted, strings);
    }

    @Test
    public void testFindCoursesById() throws Exception {
        String key = "aaaaaaa";
        List<Course> courses = findRegistryCoursesController.findCoursesById(key)
                .limit(10).collect(Collectors.toList());
        assertNotNull(courses);
        assertEquals(10, courses.size());
        float lastVal = 1.0f;
        for (Course c : courses) {
            float newVal = metric.compare(key, c.getId());
            assertTrue(newVal <= lastVal);
            lastVal = newVal;
        }
    }

    @Test
    public void testFindCoursesByName() throws Exception {
        String key = "aaaaaaa";
        List<Course> courses = findRegistryCoursesController.findCoursesByName(key, Locale.getDefault())
                .limit(10).collect(Collectors.toList());
        assertNotNull(courses);
        assertEquals(10, courses.size());
        float lastVal = 1.0f;
        for (Course c : courses) {
            float newVal = metric.compare(key, c.getName());
            assertTrue(newVal <= lastVal);
            lastVal = newVal;
        }
    }
}
