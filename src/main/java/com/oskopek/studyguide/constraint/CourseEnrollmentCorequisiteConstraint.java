package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
        List<CourseEnrollment> enrollmentsUntilNow = takeUntilSemester(semesterPlan,
                getCourseEnrollment().getSemester());
        List<Course> corequisites = new ArrayList<>(getCourseEnrollment().getCourse().getCorequisites());
        for (CourseEnrollment enrollment : enrollmentsUntilNow) {
            int found = corequisites.indexOf(enrollment.getCourse());
            if (found >= 0) {
                corequisites.remove(found);
            }
        }
        if (!corequisites.isEmpty()) {
            fireBrokenEvent(generateMessage(message, corequisites), getCourseEnrollment());
        } else {
            fireFixedEvent(this);
        }
    }
}
