package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A simple and non stable integration test for {@link SISHtmlScraper}.
 */
public class SISHtmlScraperIT {

    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    private SISHtmlScraper scraper;
    private CourseRegistry registry;

    @Before
    public void setUp() {
        registry = new CourseRegistry();
    }

    @Test
    public void testScrapeCourseMFF() throws Exception {
        scraper = new SISHtmlScraper("https://is.cuni.cz/studium/");

        Course advancedJava = scraper.scrapeCourse(registry, "NPRG021");
        assertNotNull(advancedJava);
        assertEquals(3, registry.courseMapValues().size());
        assertEquals("Advanced programming for Java platform", advancedJava.getName());
        assertEquals(6, advancedJava.getCredits().getCreditValue());
    }

    @Test
    public void testScrapeCoursesVscht() throws Exception {
        scraper = new SISHtmlScraper("https://student.vscht.cz/");

        Course biochem1 = scraper.scrapeCourse(registry, "N320001");
        assertNotNull(biochem1);
        assertEquals(1, registry.courseMapValues().size());
        assertEquals("Biochemistry I", biochem1.getName());
        assertEquals(5, biochem1.getCredits().getCreditValue());
    }

    @Test
    public void testScrapeCoursesVschtWeirdCredits() throws Exception {
        scraper = new SISHtmlScraper("https://student.vscht.cz/");

        Course clinBiochem = scraper.scrapeCourse(registry, "N320011");
        assertNotNull(clinBiochem);
        assertEquals(1, registry.courseMapValues().size());
        assertEquals("Clinical Biochemistry", clinBiochem.getName());
        assertEquals(3, clinBiochem.getCredits().getCreditValue());
    }

}
