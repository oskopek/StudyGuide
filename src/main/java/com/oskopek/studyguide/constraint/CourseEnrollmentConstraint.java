package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.courses.Course;
import org.apache.commons.lang.StringUtils;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Constraint on the level of individual {@link com.oskopek.studyguide.model.CourseEnrollment}s.
 * Is an abstract parent of constraints for checking course prerequisites and corequisites.
 */
public abstract class CourseEnrollmentConstraint extends DefaultConstraint {

    private CourseEnrollment enrollment;

    @Inject
    private Event<BrokenCourseEnrollmentConstraintEvent> brokenEvent;

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseEnrollmentConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param enrollment the enrollment to check
     */
    public CourseEnrollmentConstraint(CourseEnrollment enrollment) {
        this.enrollment = enrollment;
    }

    /**
     * Get the enrollment we're checking.
     *
     * @return the enrollment
     */
    public CourseEnrollment getEnrollment() {
        return enrollment;
    }

    /**
     * Utility method: takes semesters from the plan and while they are sooner in the plan than the given semester
     * collects all their {@link CourseEnrollment}s in a list. Simulates Haskell's takeWhile method.
     *
     * @param plan the plan from which to take semesters
     * @param semester the semester to stop collecting at (still included in the list)
     * @return the collected list
     */
    protected static List<CourseEnrollment> takeUntilSemester(SemesterPlan plan, Semester semester) {
        // TODO OPTIONAL rewrite this
        List<CourseEnrollment> enrollments = new ArrayList<>();
        for (Semester pSemester : plan) {
            enrollments.addAll(pSemester.getCourseEnrollmentList());
            if (semester.equals(pSemester)) {
                break;
            }
        }
        return enrollments;
    }

    @Override
    public void validate(@Observes Course changed) {
        semesterPlan.allCourseEnrollments().filter(ce -> changed.equals(ce.getCourse())).forEach(this::validate);
    }

    @Override
    public void fireBrokenEvent(String reason, CourseEnrollment enrollment) {
        brokenEvent.fire(new BrokenCourseEnrollmentConstraintEvent(reason, enrollment));
    }

    @Override
    public void fireBrokenEvent(String reason, Course course) {
        brokenEvent.fire(new BrokenCourseEnrollmentConstraintEvent("C", null));
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param message the message to broadcast
     * @param brokenRequirements the courses whose requirements were broken
     * @return the String to use as a message, localized
     */
    protected String generateMessage(String message, List<Course> brokenRequirements) {
        return String.format(messages.getString(message), StringUtils.join(brokenRequirements.iterator(), ", "));
    }
}
