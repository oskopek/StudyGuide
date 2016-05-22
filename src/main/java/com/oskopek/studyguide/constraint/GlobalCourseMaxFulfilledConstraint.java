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
 * Checks if the total number of fulfilled
 * {@link CourseEnrollment}s of a single {@link com.oskopek.studyguide.model.courses.Course} is at most N.
 */
public class GlobalCourseMaxFulfilledConstraint extends GlobalConstraint {

    private final String message = "constraint.globalMaxFulfilledInvalid";
    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    private int maxFulfilled;

    /**
     * Private default constructor, needed by CDI.
     */
    protected GlobalCourseMaxFulfilledConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param maxFulfilled the maximum number of times a single course can be fulfilled in a plan
     */
    public GlobalCourseMaxFulfilledConstraint(int maxFulfilled) {
        this.maxFulfilled = maxFulfilled;
    }

    @Override
    public void validate() {
        logger.trace("Validating... (max: {})", maxFulfilled);
        Map<Course, List<CourseEnrollment>> groupByCourse = semesterPlan.getSemesterList().stream()
                .flatMap(s -> s.getCourseEnrollmentList().stream()).filter(CourseEnrollment::isFulfilled)
                .collect(Collectors.groupingBy(CourseEnrollment::getCourse));
        for (Map.Entry<Course, List<CourseEnrollment>> entry : groupByCourse.entrySet()) {
            Course course = entry.getKey();
            List<CourseEnrollment> enrollments = entry.getValue();
            if (enrollments.size() > maxFulfilled) {
                logger.debug("Broken on {} (fulfilled: {}, max {})", course, enrollments.size(), maxFulfilled);
                fireBrokenEvent(generateMessage(course, enrollments.size(), maxFulfilled));
                return;
            }
        }
        fireFixedEvent(this);
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param course the course that was fulfilled too many times
     * @param got the number of times the student fulfilled the course
     * @param max the maximum number of times one can fulfill a course
     * @return the String to use as a message, localized
     */
    private String generateMessage(Course course, int got, int max) {
        return String.format(messages.getString(message), course, got, max);
    }

    /**
     * The max number of fulfilled enrollments of a specific course that still doesn't trigger this constraint.
     *
     * @return the max number of fulfilled enrollments
     */
    @JsonGetter
    public int getMaxFulfilled() {
        return maxFulfilled;
    }

    /**
     * Set the max number of fulfilled enrollments of a specific course that still doesn't trigger this constraint.
     *
     * @param maxFulfilled the max number of fulfilled enrollments to set
     */
    public void setMaxFulfilled(int maxFulfilled) {
        this.maxFulfilled = maxFulfilled;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getMaxFulfilled()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalCourseMaxFulfilledConstraint)) {
            return false;
        }
        GlobalCourseMaxFulfilledConstraint that = (GlobalCourseMaxFulfilledConstraint) o;
        return new EqualsBuilder().append(getMaxFulfilled(), that.getMaxFulfilled()).isEquals();
    }
}
