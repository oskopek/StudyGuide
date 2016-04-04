package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A simple and non stable integration test for {@link MFFHtmlScraper}.
 */
public class MFFWebScraperIT {

    private MFFHtmlScraper scraper;
    private final String mffIoiInfoUrl = "http://www.mff.cuni.cz/studium/bcmgr/ok/ib3a21.htm";
    private final String sisWebUrl = "https://is.cuni.cz/studium";
    private final String referenceFile = "src/test/resources/com/oskopek/studyguide/persistence/mff_ioi_2015_2016.json";

    @Before
    public void setUp() {
        scraper = new MFFHtmlScraper(sisWebUrl);
    }

    @Test
    public void testScrapeCourses() throws Exception {
        CourseRegistry registry = scraper.scrapeStudyPlan(mffIoiInfoUrl).getCourseRegistry();
        assertNotNull(registry);
        assertNotNull(registry.courseMapValues());
        assertEquals(65, registry.courseMapValues().size()); // 61 base from IOI + 4 required courses

        StudyPlan studyPlan = new JsonDataReaderWriter().readFrom(referenceFile);
        assertArrayEquals(studyPlan.getCourseRegistry().courseMapValues().toArray(),
                registry.courseMapValues().toArray());
    }

}
