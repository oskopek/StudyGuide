package com.oskopek.studyguide.model.courses;

import java.util.ResourceBundle;

/**
 * Semester types for constraining course enrollments.
 */
public enum EnrollableIn {

    /**
     * Winter/fall semester. Usually semesters with a semesterIndex % 2 == 1 (counted from 1).
     */
    WINTER,

    /**
     * Summer/spring semester. Usually semesters with a semesterIndex % 2 == 0 (counted from 1).
     */
    SUMMER,

    /**
     * Both of the above (covers all semesters).
     */
    BOTH;

    /**
     * Print a localized string representing the given value.
     *
     * @param messages the resource bundle to use
     * @return localized string representation
     * @throws IllegalStateException if this is not a known enrollable in type
     */
    public String print(ResourceBundle messages) {
        switch (this) {
            case WINTER:
                return messages.getString("semester.winterLong");
            case SUMMER:
                return messages.getString("semester.summerLong");
            case BOTH:
                return messages.getString("semester.bothLong");
            default:
                throw new IllegalStateException("No known enrollable in: " + this);
        }
    }
}
