package com.oskopek.studyguide.model;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Course generator util class.
 */
public final class CourseGenerator {

    private static final Random random = new Random();

    /**
     * Empty default constructor.
     */
    private CourseGenerator() {
        // intentionally empty
    }

    /**
     * Generate a course with random data.
     * @return a random course, non null.
     */
    public static Course generateRandomCourse() {
        String id = generateRandomString(4);
        String name = generateRandomString(8);
        String localizedName = generateRandomString(10);
        Locale locale = Locale.forLanguageTag("cs");
        Credits credits = Credits.valueOf(random.nextInt(10));
        List<String> teachers = new ArrayList<>();
        List<Course> prereqCourses = new ArrayList<>();
        List<Course> coreqCourses = new ArrayList<>();
        return new Course(id, name, localizedName, locale, credits, teachers, prereqCourses, coreqCourses);
    }

    /**
     * Generates a random string of a given length, with chars in the range 'a' - 'c' inclusive.
     * @param length the length of the returned string
     * @return a random string of length {@code length}
     */
    private static String generateRandomString(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (random.nextInt('d' - 'a') + 'a');
        }
        return String.valueOf(chars);
    }

}
