package com.oskopek.studyguide.model;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by skopeko on 10.3.16.
 */
public class CourseGenerator {

    private static final Random random = new Random();

    public static Course generateRandomCourse() {
        String id = generateRandomString(4);
        String name = generateRandomString(8);
        String localizedName = generateRandomString(10);
        Locale locale = Locale.forLanguageTag("cs");
        Credits credits = Credits.valueOf(random.nextInt(10));
        List<String> teachers = new ArrayList<>();
        List<Course> reqCourses = new ArrayList<>();
        return new Course(id, name, localizedName, locale, credits, teachers, reqCourses);
    }

    private static String generateRandomString(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char)(random.nextInt('c'-'a') + 'a');
        }
        return String.valueOf(chars);
    }

}
