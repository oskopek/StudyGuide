package com.oskopek.studyguide.weld;

import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.model.CourseEnrollment;
import com.oskopek.studyguide.model.courses.Course;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventBusTranslator {

    @Inject
    private EventBus eventBus;

    @Inject
    private Logger logger;

    private ChangeListener<Course> courseChangeListener = new ChangeListener<Course>() {
        @Override
        public void changed(ObservableValue<? extends Course> observable, Course oldValue, Course newValue) {
            logger.trace("Translating course change: {}", newValue);
            if (newValue != null) {
                eventBus.post(newValue);
            }
        }
    };
    private ChangeListener<CourseEnrollment> courseEnrollmentChangeListener = new ChangeListener<CourseEnrollment>() {
        @Override
        public void changed(ObservableValue<? extends CourseEnrollment> observable, CourseEnrollment oldValue,
                            CourseEnrollment newValue) {
            logger.trace("Translating course enrollment change: {}", newValue);
            if (newValue != null) {
                eventBus.post(newValue);
            }
        }
    };

    public void register(Course course) {
        logger.trace("Registering course {}", course);
        course.addListener(courseChangeListener);
    }

    public void register(CourseEnrollment enrollment) {
        logger.trace("Registering course enrollment {}", enrollment);
        enrollment.addListener(courseEnrollmentChangeListener);
    }

    public void unregister(Course course) {
        logger.trace("Unregistering course {}", course);
        course.removeListener(courseChangeListener);
    }

    public void unregister(CourseEnrollment enrollment) {
        logger.trace("Unregistering course enrollment {}", enrollment);
        enrollment.removeListener(courseEnrollmentChangeListener);
    }
}
