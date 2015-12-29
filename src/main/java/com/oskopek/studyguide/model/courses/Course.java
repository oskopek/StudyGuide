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
     * @param locale the locale of the localized course name, non-null if {@code localizedName} is non-null
     * @param credits the credits awarded after fulfilling this course, non-null
     * @param teacherNames teachers of this course
     * @param requiredCourses courses required
     * as per {@link com.oskopek.studyguide.constraints.CourseEnrollmentRequirementsUnfulfilledConstraint}
     * @throws IllegalArgumentException if id, name or credits are null
     * or if the locale is null when localizedName is non-null
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
        if (locale == null && localizedName != null) {
            throw new IllegalArgumentException("Locale cannot be null when localized name isn't null.");
        }
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
     * @return the localized name
     * @see #getLocale()
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
     * @return non-null, may be empty
     * @see com.oskopek.studyguide.constraints.CourseEnrollmentRequirementsUnfulfilledConstraint
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

    /**
     * Get a localized or default name, depending on the locale.
     *
     * @param otherLocale the locale in which to get the name
     * @return localized name iff the locale languages are the same
     */
    public String name(Locale otherLocale) {
        if (localizedName == null || otherLocale == null || !locale.getLanguage().equals(otherLocale.getLanguage())) {
            return name;
        } else {
            return localizedName;
        }
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
