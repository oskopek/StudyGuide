package com.oskopek.studyguide.constraint;

import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.model.CourseGenerator;
import com.oskopek.studyguide.model.Semester;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.courses.Credits;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class GlobalCreditsSumConstraintTest {

    GlobalCreditsSumConstraint globalCreditsSumConstraint;
    Credits totalNeeded;

    @Before
    public void setUp() throws Exception {
        globalCreditsSumConstraint = new GlobalCreditsSumConstraint();

        SemesterPlan plan = new SemesterPlan();
        Semester s0 = new Semester("0");
        Semester s1 = new Semester("1");
        Semester s2 = new Semester("2");
        plan.addSemester(s0);
        plan.addSemester(s1);
        plan.addSemester(s2);

        s0.addCourseEnrollment(CourseGenerator.generateRandomCourse());
        s0.addCourseEnrollment(CourseGenerator.generateRandomCourse());
        s1.addCourseEnrollment(CourseGenerator.generateRandomCourse());
        s2.addCourseEnrollment(CourseGenerator.generateRandomCourse());

        plan.allCourseEnrollments().forEach(courseEnrollment -> courseEnrollment.setFulfilled(true));

        totalNeeded = Credits.valueOf(plan.allCourseEnrollments()
                .mapToInt(c -> c.getCourse().getCredits().getCreditValue()).sum());

        globalCreditsSumConstraint.setTotalNeeded(totalNeeded);
        globalCreditsSumConstraint.semesterPlan = plan;

        globalCreditsSumConstraint.messages = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                return "mocked";
            }

            @Override
            public Enumeration<String> getKeys() {
                return Collections.emptyEnumeration();
            }
        };
        globalCreditsSumConstraint.eventBus = mock(EventBus.class);
    }

    @Test
    public void validate() throws Exception {
        GlobalCreditsSumConstraint spy = spy(globalCreditsSumConstraint);
        spy.validate();
        verify(spy, never()).fireBrokenEvent(anyString());
        verify(spy.eventBus, never()).post(anyObject());
    }

    @Test
    public void validateFire() throws Exception {
        globalCreditsSumConstraint.setTotalNeeded(Credits.valueOf(totalNeeded.getCreditValue() + 1));
        GlobalCreditsSumConstraint spy = spy(globalCreditsSumConstraint);
        spy.validate();
        verify(spy, atLeastOnce()).fireBrokenEvent(anyString());
        verify(spy.eventBus, atLeastOnce()).post(anyObject());
    }

}