package com.oskopek.studyguide.model;

import java.util.List;

/**
 * Single semester in a {@link StudyPlan}.
 */
public class Semester {

    private int id;
    private String name;
    private List<CourseEnrollment> courseEnrollmentList;

}
