package com.oskopek.studyguide.model.courses;

import java.util.Locale;

/**
 * Background information about a course students can enroll in. There should be only one instance of this per course.
 */
public class Course {

    private String id;
    private String name;
    private String localizedName;
    private Locale locale;
    private Credits credits;
    private String[] teacherNames;
    private Course[] requiredCourses;

}
