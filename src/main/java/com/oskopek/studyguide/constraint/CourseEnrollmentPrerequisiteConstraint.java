package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;

import javax.enterprise.event.Observes;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks if all the prerequisite courses for a {@link com.oskopek.studyguide.model.CourseEnrollment}
 * are marked as fulfilled (at the latest one semester before the current one).
 */
public class CourseEnrollmentPrerequisiteConstraint extends CourseEnrollmentConstraint {

    private final String message = "constraint.unfulfilledPrerequisite";

    private CourseEnrollmentPrerequisiteConstraint() {
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
    public void validate(@Observes CourseEnrollment courseEnrollment) {
        List<Course> corequisites = new ArrayList<>(getEnrollment().getCourse().getCorequisites());
        int semesterIndex = semesterPlan.getSemesterList().indexOf(getEnrollment().getSemester()) - 1;
        if (semesterIndex < 0) {
            if (!corequisites.isEmpty()) {
                fireBrokenEvent(generateMessage(message, corequisites), courseEnrollment);
            }
            return;
        }

        List<CourseEnrollment> enrollmentsUntilNow = takeUntilSemester(semesterPlan, semesterPlan.getSemesterList().get(semesterIndex));
        for (CourseEnrollment enrollment : enrollmentsUntilNow) {
            int found = corequisites.indexOf(enrollment.getCourse());
            if (found >= 0 && enrollment.isFulfilled()) {
                corequisites.remove(found);
            }
        }
        if (!corequisites.isEmpty()) {
            fireBrokenEvent(generateMessage(message, corequisites), courseEnrollment);
        }
    }
}
