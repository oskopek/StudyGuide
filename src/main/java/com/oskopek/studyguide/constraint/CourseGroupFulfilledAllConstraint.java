package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.constraints.CourseGroup;
import com.oskopek.studyguide.model.courses.Course;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Checks if all {@link Course}s in a given {@link CourseGroup} are fulfilled.
 */
public class CourseGroupFulfilledAllConstraint extends CourseGroupConstraint {

    private final String message = "constraint.courseGroupFulfilledAllInvalid";

    /**
     * Default constructor.
     *
     * @param courseGroup the referenced course group
     */
    public CourseGroupFulfilledAllConstraint(CourseGroup courseGroup) {
        super(courseGroup);
    }

    @Override
    public void validate() {
        Set<Course> fulfilledCompulsoryCourses = semesterPlan.allCourseEnrollments().filter(ce -> ce.isFulfilled())
                .map(ce -> ce.getCourse()).collect(Collectors.toSet());
        Set<Course> unfulfilledCompulsoryCourses = new HashSet<>(getCourseGroup().courseListProperty().get());
        unfulfilledCompulsoryCourses.removeAll(fulfilledCompulsoryCourses);
        if (unfulfilledCompulsoryCourses.size() > 0) {
            fireBrokenEvent(generateMessage(unfulfilledCompulsoryCourses));
        }
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param unfulfilled all the unfulfilled courses from the course group
     * @return the String to use as a message, localized
     */
    private String generateMessage(Collection<Course> unfulfilled) {
        return messages.getString(message) + ": "
                + String.join(", ", unfulfilled.stream().map(c -> c.getId()).collect(Collectors.toList()));
    }
}
