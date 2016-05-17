package com.oskopek.studyguide.constraint;

import com.google.common.eventbus.Subscribe;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if all the prerequisite courses for a {@link com.oskopek.studyguide.model.CourseEnrollment}
 * are marked as fulfilled (at the latest one semester before the current one).
 */
public class CourseEnrollmentPrerequisiteConstraint extends CourseEnrollmentConstraint {

    private final String message = "constraint.unfulfilledPrerequisite";

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseEnrollmentPrerequisiteConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param enrollment the enrollment to check
     */
    public CourseEnrollmentPrerequisiteConstraint(CourseEnrollment enrollment) {
        super(enrollment);
    }

    @Override
    @Subscribe
    public void validate(CourseEnrollment changed) {
        logger.get().trace("Caught event {} at {}", changed, this);
        List<Course> corequisites = new ArrayList<>(getCourseEnrollment().getCourse().getCorequisites());
        int semesterIndex = semesterPlan.get().getSemesterList().indexOf(getCourseEnrollment().getSemester()) - 1;
        if (semesterIndex < 0) {
            if (!corequisites.isEmpty()) {
                fireBrokenEvent(generateMessage(message, corequisites), changed);
            }
            return;
        }

        List<CourseEnrollment> enrollmentsUntilNow =
                takeUntilSemester(semesterPlan.get(), semesterPlan.get().getSemesterList().get(semesterIndex));
        for (CourseEnrollment enrollment : enrollmentsUntilNow) {
            int found = corequisites.indexOf(enrollment.getCourse());
            if (found >= 0 && enrollment.isFulfilled()) {
                corequisites.remove(found);
            }
        }
        if (!corequisites.isEmpty()) {
            fireBrokenEvent(generateMessage(message, corequisites), changed);
        }
    }
}
