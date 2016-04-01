package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * A simple and non stable integration test for {@link MFFWebScraper}.
 */
public class MFFWebScraperTest { // TODO create a unit test and an integration test separately

    private MFFWebScraper scraper;
    private final String mffIoiInfoUrl = "http://www.mff.cuni.cz/studium/bcmgr/ok/ib3a21.htm";
    private final String referenceFile = "src/test/resources/com/oskopek/studyguide/persistence/mff_ioi_2015_2016.json";

    @Before
    public void setUp() {
        scraper = new MFFWebScraper();
    }

    @Test
    @Ignore("Takes a long time") // TODO create a local test
    public void testScrapeCourses() throws Exception {
        CourseRegistry registry = scraper.scrapeCourses(mffIoiInfoUrl);
        assertNotNull(registry);
        assertNotNull(registry.courseMapValues());
        assertEquals(65, registry.courseMapValues().size()); // 61 base from IOI + 4 required courses

        StudyPlan studyPlan = new JsonDataReaderWriter().readFrom(referenceFile);
        assertArrayEquals(studyPlan.getCourseRegistry().courseMapValues().toArray(),
                registry.courseMapValues().toArray());
    }

    @Test
    @Ignore("Not really a test.")
    public void scrapeCoursesToFile() throws Exception {
        CourseRegistry registry = scraper.scrapeCourses(mffIoiInfoUrl);

        System.out.println(Arrays.toString(registry.courseMapValues().toArray()));
        DefaultStudyPlan studyPlan2 = new DefaultStudyPlan();
        studyPlan2.courseRegistryProperty().setValue(registry);
        new JsonDataReaderWriter().writeTo(studyPlan2, "test.json");
    }

}
