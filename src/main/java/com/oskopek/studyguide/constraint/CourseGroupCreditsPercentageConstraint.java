package com.oskopek.studyguide.constraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oskopek.studyguide.model.constraints.CourseGroup;
import com.oskopek.studyguide.model.courses.Course;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.math.Fraction;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

/**
 * Checks if the {@link com.oskopek.studyguide.model.courses.Credits} ratio fulfilled to total number of
 * {@link com.oskopek.studyguide.model.courses.Course}s in a given
 * {@link com.oskopek.studyguide.model.constraints.CourseGroup} is at least N.
 */
public class CourseGroupCreditsPercentageConstraint extends CourseGroupConstraint {

    private static DecimalFormat percentageFormat = new DecimalFormat("##0.00");
    private final String message = "constraint.courseGroupCreditsPercentageInvalid";
    private Fraction neededFraction;

    /**
     * Private default constructor, needed by CDI.
     */
    protected CourseGroupCreditsPercentageConstraint() {
        // needed by CDI
    }

    /**
     * Default constructor.
     *
     * @param courseGroup the referenced course group
     * @param neededFraction the minimum fraction of fulfilled course credits the student needs to achieve
     */
    public CourseGroupCreditsPercentageConstraint(CourseGroup courseGroup, Fraction neededFraction) {
        super(courseGroup);
        this.neededFraction = neededFraction;
    }

    /**
     * Utility method for converting {@link Fraction}s to percentage points with two decimal places.
     * To be replaced with Apache's {@code lang3} Fraction.
     *
     * @param fraction the fraction to convert
     * @return the percentage representation in a String
     */
    private static String toPercent(Fraction fraction) {
        // TODO OPTIONAL math3 fraction.percentageValue()
        return percentageFormat.format(fraction.doubleValue() * 100d) + "%";
    }

    /**
     * The fraction of fulfilled course credit sum to the total group credit sum needed to pass this constraint.
     *
     * @return the fulfilled course credit sum fraction needed
     */
    @JsonIgnore
    private Fraction getNeededFraction() {
        return neededFraction;
    }

    /**
     * Util method for Jackson, to serialize the {@link #neededFraction} in a sane way.
     *
     * @return the needed fraction in a string representation
     */
    @JsonProperty("neededFraction")
    private String getNeededFractionString() {
        return getNeededFraction().toProperString();
    }

    /**
     * Util method for Jackson, to deserialize the {@link #neededFraction} in a sane way.
     *
     * @param neededFractionString the needed fraction in a string representation
     */
    @JsonProperty("neededFraction")
    private void setNeededFractionString(String neededFractionString) {
        this.neededFraction = Fraction.getFraction(neededFractionString);
    }

    @Override
    public void validate() {
        List<Course> groupCourses = getCourseGroup().courseListProperty().get();
        Stream<Course> fulfilledGroupCourses = semesterPlan.allCourseEnrollments()
                .filter(ce -> ce.isFulfilled()).map(ce -> ce.getCourse()).filter(c -> groupCourses.contains(c));
        int creditSum = groupCourses.stream().map(c -> c.getCredits().getCreditValue()).reduce(0, Integer::sum);
        int fulfilledSum = fulfilledGroupCourses.map(c -> c.getCredits().getCreditValue()).reduce(0, Integer::sum);
        Fraction gotFraction = Fraction.getFraction(fulfilledSum, creditSum);
        if (neededFraction.compareTo(gotFraction) > 0) {
            fireBrokenEvent(generateMessage(gotFraction, neededFraction));
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getNeededFraction())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseGroupCreditsPercentageConstraint)) {
            return false;
        }
        CourseGroupCreditsPercentageConstraint that = (CourseGroupCreditsPercentageConstraint) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getNeededFraction(), that.getNeededFraction())
                .isEquals();
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param got the passed course fraction the student achieved
     * @param needed the fraction of passed courses the student needs to pass
     * @return the String to use as a message, localized
     */
    private String generateMessage(Fraction got, Fraction needed) {
        return String.format(messages.getString(message), toPercent(needed), toPercent(got));
    }
}
