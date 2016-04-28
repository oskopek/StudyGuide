package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.constraints.CourseGroup;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.Credits;

import java.util.List;
import java.util.stream.Stream;

/**
 * Checks if the {@link com.oskopek.studyguide.model.courses.Credits} sum of fulfilled
 * {@link com.oskopek.studyguide.model.courses.Course}s in a given
 * {@link com.oskopek.studyguide.model.constraints.CourseGroup} is at least N.
 */
public class CourseGroupCreditsSumConstraint extends CourseGroupConstraint {

    private Credits totalNeeded;

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
    public boolean isBroken(SemesterPlan plan) {
        List<Course> groupCourses = getCourseGroup().courseListProperty().get();
        Stream<Course> fulfilledGroupCourses = plan.getSemesterList().stream()
                .flatMap(s -> s.getCourseEnrollmentList().stream())
                .filter(ce -> ce.isFulfilled()).map(ce -> ce.getCourse()).filter(c -> groupCourses.contains(c));
        int fulfilledSum = fulfilledGroupCourses.map(c -> c.getCredits().getCreditValue()).reduce(0, Integer::sum);
        return fulfilledSum < totalNeeded.getCreditValue();
    }

    @Override
    public void fireIfBroken(SemesterPlan plan) {
        // TODO impl
    }

}
