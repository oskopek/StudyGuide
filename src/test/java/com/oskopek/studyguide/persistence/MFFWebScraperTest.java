package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by skopeko on 1.4.16.
 */
public class MFFWebScraperTest { // TODO test depends on an internet connection

    private MFFWebScraper scraper;
    private final String mffIoiInfoUrl = "http://www.mff.cuni.cz/studium/bcmgr/ok/ib3a21.htm";
    private final String referenceFile = "src/test/resources/com/oskopek/studyguide/persistence/mff_ioi_2015_2016.json";

    @Before
    public void setUp() {
        scraper = new MFFWebScraper();
    }

    @Test
    public void testScrapeCourses() throws Exception {
        CourseRegistry registry = scraper.scrapeCourses(mffIoiInfoUrl);
        assertNotNull(registry);
        assertNotNull(registry.courseMapValues());
        assertEquals(61, registry.courseMapValues().size());

        StudyPlan studyPlan = new JsonDataReaderWriter().readFrom(referenceFile);
        assertArrayEquals(studyPlan.getCourseRegistry().courseMapValues().toArray(),
                registry.courseMapValues().toArray());

        // TODO erase this
//        System.out.println(Arrays.toString(registry.courseMapValues().toArray()));
//        DefaultStudyPlan studyPlan = new DefaultStudyPlan();
//        studyPlan.courseRegistryProperty().setValue(registry);
//        new JsonDataReaderWriter().writeTo(studyPlan, "test.json");
    }

}
