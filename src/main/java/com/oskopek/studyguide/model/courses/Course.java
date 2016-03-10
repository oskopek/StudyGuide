package com.oskopek.studyguide.model.courses;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;
import java.util.Locale;

/**
 * Background information about a course students can enroll in.
 * There should be only one instance of this per course.
 */
public class Course {

    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty localizedName;
    private final ObjectProperty<Locale> locale;
    private final ObjectProperty<Credits> credits;
    private final ListProperty<String> teacherNames;
    private final ListProperty<Course> requiredCourses;

    /**
     * Empty default constructor for JSON.
     */
    private Course() {
        this.id = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.localizedName = new SimpleStringProperty();
        this.locale = new SimpleObjectProperty<>(Locale.getDefault());
        this.credits = new SimpleObjectProperty<>();
        this.teacherNames = new SimpleListProperty<>();
        this.requiredCourses = new SimpleListProperty<>();
    }

    /**
     * Create a new Course with the given parameters.
     *
     * @param id              unique course id, non-null
     * @param name            course name, non-null
     * @param localizedName   course name in a local language
     * @param locale          the locale of the localized course name, non-null if {@code localizedName} is non-null
     * @param credits         the credits awarded after fulfilling this course, non-null
     * @param teacherNames    teachers of this course
     * @param requiredCourses courses required
     * as per {@link com.oskopek.studyguide.constraints.CourseEnrollmentRequirementsUnfulfilledConstraint}
     * @throws IllegalArgumentException if id, name or credits are null
     *                                  or if the locale is null when localizedName is non-null
     */
    public Course(String id, String name, String localizedName, Locale locale, Credits credits,
                  List<String> teacherNames, List<Course> requiredCourses) throws IllegalArgumentException {
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
            this.teacherNames = new SimpleListProperty<>();
        } else {
            this.teacherNames = new SimpleListProperty<>(FXCollections.observableArrayList(teacherNames));
        }
        if (requiredCourses == null) {
            this.requiredCourses = new SimpleListProperty<>();
        } else {
            this.requiredCourses = new SimpleListProperty<>(FXCollections.observableArrayList(requiredCourses));
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
    public ObservableList<Course> getRequiredCourses() {
        return requiredCourses.get();
    }

    /**
     * Names of teachers teaching this course.
     *
     * @return non-null
     */
    public ObservableList<String> getTeacherNames() {
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
    public ListProperty<String> teacherNamesProperty() {
        return teacherNames;
    }

    /**
     * The JavaFX property for {@link #getRequiredCourses()}.
     *
     * @return the property of {@link #getRequiredCourses()}
     */
    public ListProperty<Course> requiredCoursesProperty() {
        return requiredCourses;
    }

    /**
     * Setter into {@link #creditsProperty()}.
     * @param credits non-null
     */
    public void setCredits(Credits credits) {
        if (credits == null) {
            throw new IllegalArgumentException("Credits cannot be null.");
        }
        this.credits.set(credits);
    }

    /**
     * Setter into {@link #idProperty()}.
     * @param id non-null
     */
    public void setId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null.");
        }
        this.id.set(id);
    }

    /**
     * Setter into {@link #localeProperty()}.
     * @param locale can be null
     */
    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    /**
     * Setter into {@link #localizedNameProperty()}.
     * @param localizedName can be null
     */
    public void setLocalizedName(String localizedName) {
        this.localizedName.set(localizedName);
    }

    /**
     * Setter into {@link #nameProperty()}.
     * @param name non-null
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The name cannot be null.");
        }
        this.name.set(name);
    }

    /**
     * Setter into {@link #requiredCoursesProperty()}.
     * @param requiredCourses non-null
     */
    public void setRequiredCourses(List<Course> requiredCourses) {
        if (requiredCourses == null) {
            throw new IllegalArgumentException("The required courses list cannot be null.");
        }
        this.requiredCourses.set(FXCollections.observableArrayList(requiredCourses));
    }

    /**
     * Setter into {@link #teacherNamesProperty()}.
     * @param teacherNames non-null
     */
    public void setTeacherNames(List<String> teacherNames) {
        if (teacherNames == null) {
            throw new IllegalArgumentException("The teacher names list cannot be null.");
        }
        this.teacherNames.set(FXCollections.observableArrayList(teacherNames));
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
