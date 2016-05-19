package com.oskopek.studyguide.constraint;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.oskopek.studyguide.constraint.event.StringMessageEvent;
import com.oskopek.studyguide.model.constraints.CourseGroup;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;
import java.util.stream.Stream;

/**
 * Checks if the {@link com.oskopek.studyguide.model.courses.Credits} sum of fulfilled
 * {@link com.oskopek.studyguide.model.courses.Course}s in a given
 * {@link com.oskopek.studyguide.model.constraints.CourseGroup} is at least N.
 */
public class CourseGroupCreditsSumConstraint extends CourseGroupConstraint {

    private final String message = "constraint.courseGroupCreditsSumInvalid";
    private Credits totalNeeded;

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseGroupCreditsSumConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param courseGroup the referenced course group
     * @param totalNeeded the minimum credit that the student needs to achieve in this course group
     */
    public CourseGroupCreditsSumConstraint(CourseGroup courseGroup, Credits totalNeeded) {
        super(courseGroup);
        this.totalNeeded = totalNeeded;
    }

    @Override
    public void validate() {
        List<Course> groupCourses = getCourseGroup().courseListProperty().get();
        Stream<Course> fulfilledGroupCourses =
                semesterPlan.allCourseEnrollments().filter(ce -> ce.isFulfilled()).map(ce -> ce.getCourse())
                        .filter(c -> groupCourses.contains(c));
        Credits fulfilledSum = Credits.valueOf(
                fulfilledGroupCourses.map(c -> c.getCredits().getCreditValue()).reduce(0, Integer::sum));
        if (fulfilledSum.compareTo(totalNeeded) < 0) {
            fireBrokenEvent(generateMessage(fulfilledSum, totalNeeded));
        } else {
            fireFixedEvent(this);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getTotalNeeded()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseGroupCreditsSumConstraint)) {
            return false;
        }
        CourseGroupCreditsSumConstraint that = (CourseGroupCreditsSumConstraint) o;
        return new EqualsBuilder().appendSuper(super.equals(o)).append(getTotalNeeded(), that.getTotalNeeded())
                .isEquals();
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param got the credit amount the student achieved
     * @param needed the credit amount the student needed to pass this constraint
     * @return the String to use as a message, localized
     */
    private String generateMessage(Credits got, Credits needed) {
        return String.format(messages.getString(message), needed.getCreditValue(), got.getCreditValue());
    }

    /**
     * Get the credit sum to pass this constraint.
     *
     * @return the total needed credit sum
     */
    @JsonGetter
    private Credits getTotalNeeded() {
        return totalNeeded;
    }

    /**
     * Set the credit sum to pass this constraint.
     *
     * @param totalNeeded the total needed credit sum
     */
    public void setTotalNeeded(Credits totalNeeded) {
        this.totalNeeded = totalNeeded;
    }

}
