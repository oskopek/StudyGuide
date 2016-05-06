package com.oskopek.studyguide.model.courses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Background information about a course students can enroll in.
 * There should be only one instance of this per course.
 */
public class Course extends ObservableValueBase<Course> implements Comparable<Course>, ObservableValue<Course> {

    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty localizedName;
    private final ObjectProperty<Locale> locale;
    private final ObjectProperty<Credits> credits;
    private final ListProperty<String> teacherNames;
    private final ListProperty<Course> prerequisites;
    private final ListProperty<Course> corequisites;

    /**
     * Empty default constructor for JSON.
     */
    private Course() {
        this.id = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.localizedName = new SimpleStringProperty();
        this.locale = new SimpleObjectProperty<>(Locale.getDefault());
        this.credits = new SimpleObjectProperty<>();
        this.teacherNames = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.prerequisites = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.corequisites = new SimpleListProperty<>(FXCollections.observableArrayList());
        registerChangeEventListeners();
    }

    /**
     * Create a new Course with the given parameters.
     *
     * @param id unique course id, non-null
     * @param name course name, non-null
     * @param localizedName course name in a local language
     * @param locale the locale of the localized course name, non-null if {@code localizedName} is non-null
     * @param credits the credits awarded after fulfilling this course, non-null
     * @param teacherNames teachers of this course
     * @param prerequisites courses to be fulfilled before enrolling in this course
     * @param corequisites courses to be enrolled in before (or at the same time) enrolling in this course
     * @throws IllegalArgumentException if id, name or credits are null
     * or if the locale is null when localizedName is non-null
     */
    public Course(String id, String name, String localizedName, Locale locale, Credits credits,
                  List<String> teacherNames, List<Course> prerequisites, List<Course> corequisites)
            throws IllegalArgumentException {
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
        if (prerequisites == null) {
            this.prerequisites = new SimpleListProperty<>();
        } else {
            this.prerequisites = new SimpleListProperty<>(FXCollections.observableArrayList(prerequisites));
        }
        if (corequisites == null) {
            this.corequisites = new SimpleListProperty<>();
        } else {
            this.corequisites = new SimpleListProperty<>(FXCollections.observableArrayList(corequisites));
        }
        registerChangeEventListeners();
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
     * Courses that need to be fulfilled in order to enroll in this course.
     *
     * @return non-null, may be empty
     */
    public ObservableList<Course> getPrerequisites() {
        return prerequisites.get();
    }

    /**
     * Courses required to be enrolled in before enrolling in this course.
     *
     * @return non-null, may be empty
     */
    public ObservableList<Course> getCorequisites() {
        return corequisites.get();
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
     * The JavaFX property for {@link #getPrerequisites()}.
     *
     * @return the property of {@link #getPrerequisites()}
     */
    public ListProperty<Course> prerequisitesProperty() {
        return prerequisites;
    }

    /**
     * The JavaFX property for {@link #getCorequisites()}.
     *
     * @return the property of {@link #getCorequisites()}
     */
    public ListProperty<Course> corequisitesProperty() {
        return corequisites;
    }

    /**
     * Setter into {@link #creditsProperty()}.
     *
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
     *
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
     *
     * @param locale can be null
     */
    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    /**
     * Setter into {@link #localizedNameProperty()}.
     *
     * @param localizedName can be null
     */
    public void setLocalizedName(String localizedName) {
        this.localizedName.set(localizedName);
    }

    /**
     * Setter into {@link #nameProperty()}.
     *
     * @param name non-null
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The name cannot be null.");
        }
        this.name.set(name);
    }

    /**
     * Setter into {@link #prerequisitesProperty()}.
     *
     * @param prerequisites non-null
     */
    public void setPrerequisites(List<Course> prerequisites) {
        if (prerequisites == null) {
            throw new IllegalArgumentException("The prerequisites list cannot be null.");
        }
        this.prerequisites.set(FXCollections.observableArrayList(prerequisites));
    }

    /**
     * Setter into {@link #corequisitesProperty()}.
     *
     * @param corequisites non-null
     */
    public void setCorequisites(List<Course> corequisites) {
        if (corequisites == null) {
            throw new IllegalArgumentException("The corequisites list cannot be null.");
        }
        this.corequisites.set(FXCollections.observableArrayList(corequisites));
    }

    /**
     * Setter into {@link #teacherNamesProperty()}.
     *
     * @param teacherNames non-null
     */
    public void setTeacherNames(List<String> teacherNames) {
        if (teacherNames == null) {
            throw new IllegalArgumentException("The teacher names list cannot be null.");
        }
        this.teacherNames.set(FXCollections.observableArrayList(teacherNames));
    }

    /**
     * Register {@link javafx.beans.value.ChangeListener}s to important attributes and notify of a course change
     * using {@link #fireValueChangedEvent()}.
     */
    private void registerChangeEventListeners() {
        id.addListener((x, y, z) -> fireValueChangedEvent());
        //        name.addListener((x, y, z) -> fireValueChangedEvent());
        //        localizedName.addListener((x, y, z) -> fireValueChangedEvent());
        //        locale.addListener((x, y, z) -> fireValueChangedEvent());
        credits.addListener((x, y, z) -> fireValueChangedEvent());
        //        teacherNames.addListener((x, y, z) -> fireValueChangedEvent());
        prerequisites.addListener((x, y, z) -> fireValueChangedEvent());
        corequisites.addListener((x, y, z) -> fireValueChangedEvent());
    }

    @Override
    public int compareTo(Course o) {
        return new CompareToBuilder().append(id, o.id).toComparison();
    }

    /**
     * Creates a shallow copy of the given Course. Used for events.
     *
     * @param original the course to copy
     * @return a new Course copy
     * @see #fireValueChangedEvent()
     */
    public static Course copy(Course original) {
        String id = original.getId();
        String name = original.getName();
        String localizedName = original.getLocalizedName();
        Locale locale = original.getLocale();
        Credits credits = original.getCredits();
        List<String> teacherNames = new ArrayList<>(original.getTeacherNames());
        List<Course> prerequisites = new ArrayList<>(original.getPrerequisites());
        List<Course> corequisites = new ArrayList<>(original.getCorequisites());
        return new Course(id, name, localizedName, locale, credits, teacherNames, prerequisites, corequisites);
    }

    @Override
    @JsonIgnore
    public Course getValue() {
        return Course.copy(this);
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
                .append(getPrerequisites(), course.getPrerequisites())
                .append(getCorequisites(), course.getCorequisites()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).append(getName()).append(getLocalizedName())
                .append(getLocale()).append(getCredits()).append(getTeacherNames()).append(getPrerequisites())
                .append(getCorequisites()).toHashCode();
    }

    @Override
    public String toString() {
        return "Course[" + getId() + ": " + getName() + ']';
    }
}
