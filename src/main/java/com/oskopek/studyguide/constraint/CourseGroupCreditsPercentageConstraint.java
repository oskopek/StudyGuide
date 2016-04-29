package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.constraints.CourseGroup;
import com.oskopek.studyguide.model.courses.Course;
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

    private Fraction neededFraction;
    private String message = "%constraint.coursegroupcreditspercentageinvalid"; // TODO expand
    private static DecimalFormat percentageFormat = new DecimalFormat("##0.00");

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

    @Override
    public void validate() {
        List<Course> groupCourses = getCourseGroup().courseListProperty().get();
        Stream<Course> fulfilledGroupCourses = semesterPlan.getSemesterList().stream()
                .flatMap(s -> s.getCourseEnrollmentList().stream())
                .filter(ce -> ce.isFulfilled()).map(ce -> ce.getCourse()).filter(c -> groupCourses.contains(c));
        int creditSum = groupCourses.stream().map(c -> c.getCredits().getCreditValue()).reduce(0, Integer::sum);
        int fulfilledSum = fulfilledGroupCourses.map(c -> c.getCredits().getCreditValue()).reduce(0, Integer::sum);
        Fraction gotFraction = Fraction.getFraction(fulfilledSum, creditSum);
        if (neededFraction.compareTo(gotFraction) > 0) {
            fireBrokenEvent(generateMessage(message, gotFraction, neededFraction));
        }
    }

    private static String generateMessage(String message, Fraction got, Fraction needed) {
        // TODO expand first
        return String.format(message, toPercent(needed), toPercent(got));
    }

    private static String toPercent(Fraction fraction) {
        // TODO math3 fraction.percentageValue()
        return percentageFormat.format(fraction.doubleValue()*100d);
    }
}
