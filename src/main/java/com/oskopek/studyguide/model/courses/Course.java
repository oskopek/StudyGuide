package com.oskopek.studyguide.model.courses;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Locale;

/**
 * Background information about a course students can enroll in. There should be only one instance of this per course.
 */
public class Course {

    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty localizedName;
    private final ObjectProperty<Locale> locale;
    private final ObjectProperty<Credits> credits;
    private final ObjectProperty<String[]> teacherNames;
    private final ObjectProperty<Course[]> requiredCourses;

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
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.localizedName = new SimpleStringProperty(localizedName);
        this.locale = new SimpleObjectProperty<>(locale);
        if (locale == null && localizedName != null) {
            throw new IllegalArgumentException("Locale cannot be null when localized name isn't null.");
        }
        this.credits = new SimpleObjectProperty<>(credits);
        if (teacherNames == null) {
            this.teacherNames = new SimpleObjectProperty<>(new String[0]);
        } else {
            this.teacherNames = new SimpleObjectProperty<>(teacherNames);
        }
        if (requiredCourses == null) {
            this.requiredCourses = new SimpleObjectProperty<>(new Course[0]);
        } else {
            this.requiredCourses = new SimpleObjectProperty<>(requiredCourses);
        }
    }

    /**
     * Credits awarded after fulfilling this course.
     *
     * @return non-null
     */
    public Credits getCredits() {
        return credits.get();
    }

    /**
     * Unique string identificator of this course.
     *
     * @return non-null
     */
    public String getId() {
        return id.get();
    }

    /**
     * Locale of {@link #getLocalizedName()}.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale.get();
    }

    /**
     * Localized {@link #getName()} of this course.
     *
     * @return the localized name
     * @see #getLocale()
     */
    public String getLocalizedName() {
        return localizedName.get();
    }

    /**
     * The name of this course.
     *
     * @return non-null
     */
    public String getName() {
        return name.get();
    }

    /**
     * Courses required to fulfill this course.
     *
     * @return non-null, may be empty
     * @see com.oskopek.studyguide.constraints.CourseEnrollmentRequirementsUnfulfilledConstraint
     */
    public Course[] getRequiredCourses() {
        return requiredCourses.get();
    }

    /**
     * Names of teachers teaching this course.
     *
     * @return non-null
     */
    public String[] getTeacherNames() {
        return teacherNames.get();
    }

    /**
     * Get the localized or default name, depending on the locale.
     *
     * @param otherLocale the locale in which to get the name
     * @return localized name iff the locale languages are the same
     */
    public String name(Locale otherLocale) {
        if (getLocalizedName() == null || otherLocale == null
                || !getLocale().getLanguage().equals(otherLocale.getLanguage())) {
            return getName();
        } else {
            return getLocalizedName();
        }
    }

    /**
     * Get the localized or default name property, depending on the locale.
     *
     * @param otherLocale the locale in which to get the name property
     * @return localized name property iff the locale languages are the same
     */
    public StringProperty nameProperty(Locale otherLocale) {
        if (getLocalizedName() == null || otherLocale == null
                || !getLocale().getLanguage().equals(otherLocale.getLanguage())) {
            return nameProperty();
        } else {
            return localizedNameProperty();
        }
    }

    /**
     * The JavaFX property for {@link #getId()}.
     *
     * @return the property of {@link #getId()}
     */
    public StringProperty idProperty() {
        return id;
    }

    /**
     * The JavaFX property for {@link #getName()}.
     *
     * @return the property of {@link #getName()}
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * The JavaFX property for {@link #getLocalizedName()}.
     *
     * @return the property of {@link #getLocalizedName()}
     */
    public StringProperty localizedNameProperty() {
        return localizedName;
    }

    /**
     * The JavaFX property for {@link #getLocale()}.
     *
     * @return the property of {@link #getLocale()}
     */
    public ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    /**
     * The JavaFX property for {@link #getCredits()}.
     *
     * @return the property of {@link #getCredits()}
     */
    public ObjectProperty<Credits> creditsProperty() {
        return credits;
    }

    /**
     * The JavaFX property for {@link #getTeacherNames()}.
     *
     * @return the property of {@link #getTeacherNames()}
     */
    public ObjectProperty<String[]> teacherNamesProperty() {
        return teacherNames;
    }

    /**
     * The JavaFX property for {@link #getRequiredCourses()}.
     *
     * @return the property of {@link #getRequiredCourses()}
     */
    public ObjectProperty<Course[]> requiredCoursesProperty() {
        return requiredCourses;
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

        return new EqualsBuilder().append(getId(), course.getId()).append(getName(), course.getName())
                .append(getLocalizedName(), course.getLocalizedName()).append(getLocale(), course.getLocale())
                .append(getCredits(), course.getCredits()).append(getTeacherNames(), course.getTeacherNames())
                .append(getRequiredCourses(), course.getRequiredCourses()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getName()).append(getLocalizedName())
                .append(getLocale()).append(getCredits()).append(getTeacherNames()).append(getRequiredCourses())
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Course[" + getId() + ": " + getName() + ']';
    }
}
