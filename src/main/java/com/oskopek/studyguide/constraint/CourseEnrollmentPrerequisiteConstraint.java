package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if all the prerequisite courses for a {@link com.oskopek.studyguide.model.CourseEnrollment}
 * are marked as fulfilled (at the latest one semester before the current one).
 */
public class CourseEnrollmentPrerequisiteConstraint extends CourseEnrollmentConstraint {

    private final String message = "constraint.unfulfilledPrerequisite";

    private final transient Logger logger = LoggerFactory.getLogger(getClass());

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
    public void validate() {
        List<Course> prerequisites = new ArrayList<>(getCourseEnrollment().getCourse().getPrerequisites());
        int semesterIndex = semesterPlan.getSemesterList().indexOf(getCourseEnrollment().getSemester()) - 1;
        if (semesterIndex < 0) {
            if (!prerequisites.isEmpty()) {
                fireBrokenEvent(generateMessage(message, prerequisites), getCourseEnrollment());
            }
            return;
        }

        List<CourseEnrollment> enrollmentsUntilNow = takeUntilSemester(semesterPlan,
                semesterPlan.getSemesterList().get(semesterIndex));
        for (CourseEnrollment enrollment : enrollmentsUntilNow) {
            int found = prerequisites.indexOf(enrollment.getCourse());
            if (found >= 0 && enrollment.isFulfilled()) {
                prerequisites.remove(found);
            }
        }
        if (!prerequisites.isEmpty()) {
            fireBrokenEvent(generateMessage(message, prerequisites), getCourseEnrollment());
        } else {
            fireFixedEvent(this);
        }
    }
}
