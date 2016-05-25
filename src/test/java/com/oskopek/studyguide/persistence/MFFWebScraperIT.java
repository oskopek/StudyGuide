package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A simple and non stable integration test for {@link MFFHtmlScraper}.
 */
public class MFFWebScraperIT {

    private final String sisWebUrl = "https://is.cuni.cz/studium";
    private final String mffUrlBase = "http://www.mff.cuni.cz/studium/bcmgr/ok/";
    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    private MFFHtmlScraper scraper;

    @Before
    public void setUp() {
        scraper = new MFFHtmlScraper(sisWebUrl);
    }

    @Test
    public void testScrapeCoursesBc() throws Exception {
        String refFileBase = "src/test/resources/com/oskopek/studyguide/persistence/";
        String[] refFiles = {"mff_bc_ioi_2015_2016.json", "mff_bc_ipss_2015_2016.json", "mff_bc_isdi_2015_2016.json"};
        String[] urlExt = {"ib3a21.htm", "ib3a22.htm", "ib3a23.htm"};
        scrapeAll(mffUrlBase, urlExt, refFileBase, refFiles);
    }

    @Test
    public void testScrapeCoursesMgr() throws Exception {
        String refFileBase = "src/test/resources/com/oskopek/studyguide/persistence/";
        String[] refFiles = {"mff_mgr_idm_2015_2016.json", "mff_mgr_iti_2015_2016.json", "mff_mgr_isdim_2015_2016.json",
                "mff_mgr_iss_2015_2016.json", "mff_mgr_iml_2015_2016.json", "mff_mgr_iui_2015_2016.json",
                "mff_mgr_ipgvph_2015_2016.json"};
        String[] urlExt = {"i3b21.htm", "i3b22.htm", "i3b23.htm", "i3b24.htm", "i3b25.htm", "i3b26.htm", "i3b27.htm"};
        scrapeAll(mffUrlBase, urlExt, refFileBase, refFiles);
    }

    @Test
    @Ignore("Is not really a test, downloads all study plans and saves them")
    public void scrapeAndSaveAllParallel() throws Exception {
        String[] urlExt = {"ib3a21.htm", "ib3a22.htm", "ib3a23.htm", "i3b21.htm", "i3b22.htm", "i3b23.htm", "i3b24.htm",
                "i3b25.htm", "i3b26.htm", "i3b27.htm"};
        String refFileBase = "src/test/resources/com/oskopek/studyguide/persistence/";
        String[] refFiles = {"mff_bc_ioi_2015_2016.json", "mff_bc_ipss_2015_2016.json", "mff_bc_isdi_2015_2016.json",
                "mff_mgr_idm_2015_2016.json", "mff_mgr_iti_2015_2016.json", "mff_mgr_isdim_2015_2016.json",
                "mff_mgr_iss_2015_2016.json", "mff_mgr_iml_2015_2016.json", "mff_mgr_iui_2015_2016.json",
                "mff_mgr_ipgvph_2015_2016.json"};
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < urlExt.length; i++) {
            logger.debug("Scheduling scrape for plan {}...", refFiles[i]);
            String url = mffUrlBase + urlExt[i];
            String refFile = refFileBase + refFiles[i];
            executorService.submit(() -> scrapeAndSave(url, refFile));
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
    }

    private void scrapeAll(String urlBase, String[] urlExt, String refFileBase, String[] refFiles) {
        int shortenedLength = 1; // urlExt.length;
        for (int i = 0; i < shortenedLength; i++) {
            logger.debug("Scraping plan {}...", refFiles[i]);
            String url = urlBase + urlExt[i];
            String refFile = refFileBase + refFiles[i];
            scrapeAndVerify(url, refFile);
        }
    }

    private void scrapeAndVerify(String url, String referenceFile) {
        logger.debug("Scraping plan: {}", referenceFile);
        StudyPlan studyPlan;
        try {
            studyPlan = scraper.scrapeStudyPlan(url);
        } catch (IOException e) {
            logger.error("An exception occurred while scraping and verifying plan {}: {}", referenceFile, e);
            fail();
            return;
        }
        StudyPlan localStudyPlan;
        try {
            localStudyPlan = new JsonDataReaderWriter().readFrom(referenceFile);
        } catch (IOException e) {
            logger.error("An exception occurred while comparing to reference file {}: {}", referenceFile, e);
            fail();
            return;
        }
        assertEquals(studyPlan.getCourseRegistry(), localStudyPlan.getCourseRegistry());
        assertEquals(studyPlan.getConstraints(), localStudyPlan.getConstraints());
        assertEquals(studyPlan.getSemesterPlan(), localStudyPlan.getSemesterPlan());
        assertEquals(studyPlan, localStudyPlan);
    }

    private void scrapeAndSave(String url, String referenceFile) {
        logger.debug("Scraping plan: {}", referenceFile);
        StudyPlan studyPlan;
        try {
            studyPlan = scraper.scrapeStudyPlan(url);
        } catch (IOException e) {
            logger.error("An exception occurred while scraping and saving plan {}: {}", referenceFile, e);
            fail();
            return;
        }
        CourseRegistry registry = studyPlan.getCourseRegistry();
        assertNotNull(registry);
        assertNotNull(registry.courseMapValues());
        try {
            new JsonDataReaderWriter().writeTo(studyPlan, referenceFile);
        } catch (IOException e) {
            logger.error("An exception occurred while comparing to reference file {}: {}", referenceFile, e);
            fail();
        }
    }

}
