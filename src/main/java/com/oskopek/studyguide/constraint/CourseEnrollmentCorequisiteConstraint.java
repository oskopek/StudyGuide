package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;

import javax.enterprise.event.Observes;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks if all the corequisite courses for a {@link com.oskopek.studyguide.model.CourseEnrollment}
 * are enrolled in (at the latest this current semester).
 */
public class CourseEnrollmentCorequisiteConstraint extends CourseEnrollmentConstraint {

    // TODO OPTIONAL merge this with Prereq constraint (more efficient)
    private final String message = "constraint.unfulfilledCorequisite";

    /**
     * Private default constructor, needed by CDI.
     */
    private CourseEnrollmentCorequisiteConstraint() {
        // needed by CDI
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
    public void validate(@Observes CourseEnrollment courseEnrollment) {
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
            fireBrokenEvent(generateMessage(message, corequisites), courseEnrollment);
        }
    }
}
