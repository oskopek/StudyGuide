package com.oskopek.studyguide.constraint;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.oskopek.studyguide.constraint.event.StringMessageEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Checks if the number of times a student is enrolled in a course is less than or equal to N.
 */
public class GlobalCourseRepeatedEnrollmentConstraint extends GlobalConstraint {

    private static final String message = "constraint.globalCourseRepeatedEnrollmentInvalid";
    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    private int maxRepeatedEnrollment;

    /**
     * Private default constructor, needed by CDI.
     */
    protected GlobalCourseRepeatedEnrollmentConstraint() {
        // needed for CDI
    }

    /**
     * Default course.
     *
     * @param maxRepeatedEnrollment the maximum count of course enrollments per course
     */
    public GlobalCourseRepeatedEnrollmentConstraint(int maxRepeatedEnrollment) {
        this.maxRepeatedEnrollment = maxRepeatedEnrollment;
    }

    @Override
    public void validate() {
        logger.trace("Validating... (max: {})", maxRepeatedEnrollment);
        Map<Course, List<CourseEnrollment>> groupByCourse = semesterPlan.getSemesterList().stream()
                .flatMap(s -> s.getCourseEnrollmentList().stream())
                .collect(Collectors.groupingBy(CourseEnrollment::getCourse));
        for (Map.Entry<Course, List<CourseEnrollment>> entry : groupByCourse.entrySet()) {
            Course course = entry.getKey();
            List<CourseEnrollment> enrollments = entry.getValue();
            if (enrollments.size() > maxRepeatedEnrollment) {
                logger.debug("Broken on {} (enrolled: {}, max {})", course, enrollments.size(), maxRepeatedEnrollment);
                fireBrokenEvent(generateMessage(course, enrollments.size(), maxRepeatedEnrollment));
                return;
            }
        }
        fireFixedEvent(this);
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param course the course that was enrolled into too many times
     * @param enrolledTimes the number of times the student enrolled in the course
     * @param maxRepeatedEnrollment the maximum number of times one can enroll into a given course
     * @return the String to use as a message, localized
     */
    private String generateMessage(Course course, int enrolledTimes, int maxRepeatedEnrollment) {
        return String.format(messages.getString(message), course.getName(), enrolledTimes, maxRepeatedEnrollment);
    }

    /**
     * The max number of repeated enrollments of a specific course that still doesn't trigger this constraint.
     *
     * @return the max number of repeated enrollments
     */
    @JsonGetter
    private int getMaxRepeatedEnrollment() {
        return maxRepeatedEnrollment;
    }

    /**
     * Set the max number of repeated enrollments of a specific course that still doesn't trigger this constraint.
     *
     * @param maxRepeatedEnrollment the max number of repeated enrollments to set
     */
    public void setMaxRepeatedEnrollment(int maxRepeatedEnrollment) {
        this.maxRepeatedEnrollment = maxRepeatedEnrollment;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getMaxRepeatedEnrollment()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalCourseRepeatedEnrollmentConstraint)) {
            return false;
        }
        GlobalCourseRepeatedEnrollmentConstraint that = (GlobalCourseRepeatedEnrollmentConstraint) o;
        return new EqualsBuilder().append(getMaxRepeatedEnrollment(), that.getMaxRepeatedEnrollment()).isEquals();
    }
}
