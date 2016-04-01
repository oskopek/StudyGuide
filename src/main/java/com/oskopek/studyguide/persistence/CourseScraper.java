package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.CourseRegistry;

import java.io.IOException;

/**
 * Interface for all batch course importing classes. The format of url is implementation specific.
 */
public interface CourseScraper {

    /**
     * Scrape the resource for courses.
     * @param url the url to scrape from
     * @return an initialized and filled out course registry
     * @throws IOException if an exception during loading of resources happened
     */
    CourseRegistry scrapeCourses(String url) throws IOException;

    /**
     * Scrape the resource for a complete study plan, with constraints.
     * May be left unimplemented (in which case it throws a {@link UnsupportedOperationException}).
     * @param url the url to scrape from
     * @return an initialized study plan
     * @throws IOException if an exception during loading of resources happened
     */
    default StudyPlan scrapeStudyPlan(String url) throws IOException {
        throw new UnsupportedOperationException("Scrape study plan not implemented!");
    }

}
