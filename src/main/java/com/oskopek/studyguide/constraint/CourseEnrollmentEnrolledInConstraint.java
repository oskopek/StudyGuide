package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.constraint.event.StringMessageEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.courses.EnrollableIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks if the course is enrolled only in the allowed semester.
 *
 * @see EnrollableIn
 */
public class CourseEnrollmentEnrolledInConstraint extends CourseEnrollmentConstraint {

    private final String message = "constraint.enrolledInInvalid";

    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseEnrollmentEnrolledInConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param enrollment the enrollment to check
     */
    public CourseEnrollmentEnrolledInConstraint(CourseEnrollment enrollment) {
        super(enrollment);
    }

    @Override
    public void validate() {
        EnrollableIn enrollableIn = getCourseEnrollment().getCourse().getEnrollableIn();
        if (EnrollableIn.BOTH.equals(enrollableIn)) {
            fireFixedEvent(this);
            return;
        }
        Semester semester = getCourseEnrollment().getSemester();
        int semesterIndex = getSemesterPlan().getSemesterList().indexOf(semester);
        if (semesterIndex < 0) {
            throw new IllegalStateException("Semester " + semester + " not found in semester plan " + semesterPlan);
        }

        semesterIndex += 1; // count from one
        switch (enrollableIn) {
            case BOTH:
                fireFixedEvent(this);
                return;
            case SUMMER:
                if (semesterIndex % 2 == 0) {
                    fireFixedEvent(this);
                    return;
                }
                break;
            case WINTER:
                if (semesterIndex % 2 == 1) {
                    fireFixedEvent(this);
                    return;
                }
                break;
            default:
                throw new IllegalStateException("Unknown enrollableIn: " + enrollableIn);
        }
        fireBrokenEvent(generateMessage(semesterIndex % 2 == 1 ? EnrollableIn.WINTER : EnrollableIn.SUMMER,
                enrollableIn), getCourseEnrollment());
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param enrolledIn the type of semester the course is currently enrolled in
     * @param enrollableIn the allowed type
     * @return the String to use as a message, localized
     */
    protected String generateMessage(EnrollableIn enrolledIn, EnrollableIn enrollableIn) {
        return String.format(messages.getString(message), enrolledIn, enrollableIn);
    }
}
