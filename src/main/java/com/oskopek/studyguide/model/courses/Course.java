package com.oskopek.studyguide.model.courses;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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

    /**
     * Create a new Course with the given parameters.
     *
     * @param id unique course id, non-null
     * @param name course name, non-null
     * @param localizedName course name in a local language
     * @param locale the locale of the localized course name
     * @param credits the credits awarded after fulfilling this course, non-null
     * @param teacherNames teachers of this course
     * @param requiredCourses courses required
     * as per {@link com.oskopek.studyguide.constraints.CourseEnrollmentRequirementsUnfulfilledConstraint}
     * @throws IllegalArgumentException if id, name or credits are null
     */
    public Course(String id, String name, String localizedName, Locale locale, Credits credits, String[] teacherNames,
            Course[] requiredCourses) throws IllegalArgumentException {
        if (id == null || name == null || credits == null) {
            throw new IllegalArgumentException("Id, name and credits cannot be null.");
        }
        this.id = id;
        this.name = name;
        this.localizedName = localizedName;
        this.locale = locale;
        this.credits = credits;
        this.teacherNames = teacherNames;
        if (teacherNames == null) {
            this.teacherNames = new String[0];
        }
        this.requiredCourses = requiredCourses;
        if (requiredCourses == null) {
            this.requiredCourses = new Course[0];
        }
    }

    /**
     * Credits awarded after fulfilling this course.
     *
     * @return non-null
     */
    public Credits getCredits() {
        return credits;
    }

    /**
     * Unique string identificator of this course.
     *
     * @return non-null
     */
    public String getId() {
        return id;
    }

    /**
     * Locale of {@link #getLocalizedName()}.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Localized {@link #getName()} of this course.
     *
     * @see #getLocale()
     * @return the localized name
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * The name of this course.
     *
     * @return non-null
     */
    public String getName() {
        return name;
    }

    /**
     * Courses required to fulfill this course.
     *
     * @see com.oskopek.studyguide.constraints.CourseEnrollmentRequirementsUnfulfilledConstraint
     * @return non-null, may be empty
     */
    public Course[] getRequiredCourses() {
        return requiredCourses;
    }

    /**
     * Names of teachers teaching this course.
     *
     * @return non-null
     */
    public String[] getTeacherNames() {
        return teacherNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        Course course = (Course) o;
        return new EqualsBuilder().append(id, course.id).append(name, course.name)
                .append(localizedName, course.localizedName).append(locale, course.locale)
                .append(credits, course.credits).append(teacherNames, course.teacherNames)
                .append(requiredCourses, course.requiredCourses).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(localizedName).append(locale).append(credits)
                .append(teacherNames).append(requiredCourses).toHashCode();
    }

    @Override
    public String toString() {
        return "Course[" + id + ": " + name + ']';
    }
}
