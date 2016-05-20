package com.oskopek.studyguide.constraint;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.oskopek.studyguide.constraint.event.StringMessageEvent;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Credits;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks if the total {@link Credits} sum of fulfilled
 * {@link com.oskopek.studyguide.model.courses.Course}s until the M-th semester (counted from 1) is at least N.
 */
public class GlobalCreditsSumUntilSemesterConstraint extends GlobalConstraint {

    private final String message = "constraint.globalCreditsSumUntilSemesterInvalid";
    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    private Credits totalNeeded;
    private int untilSemester; // counted from 1

    /**
     * Private default constructor, needed by CDI.
     */
    protected GlobalCreditsSumUntilSemesterConstraint() {
        // needed for CDI
    }

    /**
     * Default constructor.
     *
     * @param totalNeeded the minimum credit amount the student needs to achieve by the given semester
     * @param untilSemester the semester (counted from 1)
     */
    public GlobalCreditsSumUntilSemesterConstraint(Credits totalNeeded, int untilSemester) {
        this.totalNeeded = totalNeeded;
        this.untilSemester = untilSemester;
    }

    @Override
    public void validate() {
        logger.debug("Validating sum >= {} until semester {}", totalNeeded, untilSemester);
        if (semesterPlan.getSemesterList().size() < untilSemester) {
            fireFixedEvent(this);
            return;
        }

        Credits fulfilledCourseCreditSum = Credits.valueOf(
                takeUntilSemester(semesterPlan, untilSemester)
                        .filter(CourseEnrollment::isFulfilled).map(ce -> ce.getCourse().getCredits().getCreditValue())
                        .reduce(0, Integer::sum));
        if (fulfilledCourseCreditSum.compareTo(totalNeeded) < 0) {
            logger.debug("Broken sum >= {} until semester {}", totalNeeded, untilSemester);
            fireBrokenEvent(generateMessage(fulfilledCourseCreditSum, totalNeeded, untilSemester));
        } else {
            fireFixedEvent(this);
        }
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param got the amount of credit the student achieved
     * @param needed the credit amount the student needed to achieve to pass this constraint
     * @param untilSemester the semester (ordinal number) until which the amount of credits need to be achieved
     * @return the String to use as a message, localized
     */
    private String generateMessage(Credits got, Credits needed, int untilSemester) {
        return String.format(messages.getString(message), needed.getCreditValue(), got.getCreditValue(), untilSemester);
    }

    /**
     * The credit sum to pass this constraint.
     *
     * @return the total needed credit sum
     */
    @JsonGetter
    private Credits getTotalNeeded() {
        return totalNeeded;
    }

    /**
     * The credit sum to pass this constraint.
     *
     * @param totalNeeded the total needed credit sum
     */
    public void setTotalNeeded(Credits totalNeeded) {
        this.totalNeeded = totalNeeded;
    }

    /**
     * Get the semester by which the total needed credit amount must be achieved.
     *
     * @return the semester (counted from 1)
     */
    @JsonGetter
    public int getUntilSemester() {
        return untilSemester;
    }

    /**
     * Set the semester by which the total needed credit amount must be achieved.
     *
     * @param untilSemester the semester (counted from 1)
     */
    public void setUntilSemester(int untilSemester) {
        this.untilSemester = untilSemester;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getTotalNeeded()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalCreditsSumUntilSemesterConstraint)) {
            return false;
        }
        GlobalCreditsSumUntilSemesterConstraint that = (GlobalCreditsSumUntilSemesterConstraint) o;
        return new EqualsBuilder().append(getTotalNeeded(), that.getTotalNeeded()).isEquals();
    }
}
