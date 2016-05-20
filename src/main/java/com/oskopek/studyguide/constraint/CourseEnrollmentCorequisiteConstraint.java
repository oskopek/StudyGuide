package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Checks if all the corequisite courses for a {@link com.oskopek.studyguide.model.CourseEnrollment}
 * are enrolled in (at the latest this current semester).
 */
public class CourseEnrollmentCorequisiteConstraint extends CourseEnrollmentConstraint {

    // TODO OPTIONAL merge this with Prereq constraint (more efficient)
    private final String message = "constraint.unfulfilledCorequisite";

    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseEnrollmentCorequisiteConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param enrollment the enrollment to check
     */
    public CourseEnrollmentCorequisiteConstraint(CourseEnrollment enrollment) {
        super(enrollment);
    }

    @Override
    public void validate() {
        Set<Course> corequisites = new HashSet<>(getCourseEnrollment().getCourse().getCorequisites());
        Stream<Course> coursesUntilNow = takeUntilSemester(semesterPlan,
                getCourseEnrollment().getSemester()).map(CourseEnrollment::getCourse);
        Set<Course> corequisitesUntilNow = coursesUntilNow.filter(corequisites::contains).collect(Collectors.toSet());
        corequisites.removeAll(corequisitesUntilNow);
        if (!corequisites.isEmpty()) {
            fireBrokenEvent(generateMessage(message, corequisites), getCourseEnrollment());
        } else {
            fireFixedEvent(this);
        }
    }
}
