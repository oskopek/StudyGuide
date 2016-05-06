package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * A simple and non stable integration test for {@link MFFHtmlScraper}.
 */
public class MFFWebScraperIT {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private MFFHtmlScraper scraper;
    private final String sisWebUrl = "https://is.cuni.cz/studium";
    private final String mffUrlBase = "http://www.mff.cuni.cz/studium/bcmgr/ok/";

    @Before
    public void setUp() {
        scraper = new MFFHtmlScraper(sisWebUrl);
    }

    @Test
    public void testScrapeCoursesBc() throws Exception {
        String refFileBase = "src/test/resources/com/oskopek/studyguide/persistence/";
        String[] refFiles = {"mff_bc_ioi_2015_2016.json", "mff_bc_ipss_2015_2016.json", "mff_bc_isdi_2015_2016.json"};
        String[] urlExt = {"ib3a21.htm", "ib3a22.htm", "ib3a23.htm"};
        scrapeAllParallel(mffUrlBase, urlExt, refFileBase, refFiles);
    }

    @Test
    public void testScrapeCoursesMgr() throws Exception {
        String refFileBase = "src/test/resources/com/oskopek/studyguide/persistence/";
        String[] refFiles = {"mff_mgr_idm_2015_2016.json", "mff_mgr_iti_2015_2016.json",
                "mff_mgr_isdim_2015_2016.json", "mff_mgr_iss_2015_2016.json", "mff_mgr_iml_2015_2016.json",
                "mff_mgr_iui_2015_2016.json", "mff_mgr_ipgvph_2015_2016.json"};
        String[] urlExt = {"i3b21.htm", "i3b22.htm", "i3b23.htm", "i3b24.htm", "i3b25.htm", "i3b26.htm", "i3b27.htm"};
        scrapeAllParallel(mffUrlBase, urlExt, refFileBase, refFiles);
    }

    private void scrapeAll(String urlBase, String[] urlExt, String refFileBase, String[] refFiles) throws Exception {
        int shortenedLength = 1; // urlExt.length;
        for (int i = 0; i < shortenedLength; i++) {
            logger.debug("Scraping plan {}...", refFiles[i]);
            String url = urlBase + urlExt[i];
            String refFile = refFileBase + refFiles[i];
            scrapeAndVerify(url, refFile);
        }
    }

    private void scrapeAllParallel(String urlBase, String[] urlExt, String refFileBase,
                                   String[] refFiles) throws Exception {
        int shortenedLength = 1; // urlExt.length;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < shortenedLength; i++) {
            logger.debug("Scheduling scrape for plan {}...", refFiles[i]);
            String url = urlBase + urlExt[i];
            String refFile = refFileBase + refFiles[i];
            executorService.submit(() -> scrapeAndVerify(url, refFile));
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
    }

    private void scrapeAndVerify(String url, String referenceFile) {
        logger.debug("Scraping plan: {}", referenceFile);
        CourseRegistry registry;
        try {
            registry = scraper.scrapeStudyPlan(url).getCourseRegistry();
        } catch (IOException e) {
            logger.error("An exception occurred while scraping plan {}: {}", referenceFile, e);
            fail();
            return;
        }
        assertNotNull(registry);
        assertNotNull(registry.courseMapValues());
        StudyPlan studyPlan;
        try {
            studyPlan = new JsonDataReaderWriter().readFrom(referenceFile);
        } catch (IOException e) {
            logger.error("An exception occurred while comparing to reference file {}: {}", referenceFile, e);
            fail();
            return;
        }
        assertArrayEquals(studyPlan.getCourseRegistry().courseMapValues().toArray(),
                registry.courseMapValues().toArray());
    }

    private void scrapeAndSave(String url, String referenceFile) {
        logger.debug("Scraping plan: {}", referenceFile);
        CourseRegistry registry;
        try {
            registry = scraper.scrapeStudyPlan(url).getCourseRegistry();
        } catch (IOException e) {
            logger.error("An exception occurred while scraping plan {}: {}", referenceFile, e);
            fail();
            return;
        }
        assertNotNull(registry);
        assertNotNull(registry.courseMapValues());
        DefaultStudyPlan sp = new DefaultStudyPlan();
        sp.courseRegistryProperty().setValue(registry);
        try {
            new JsonDataReaderWriter().writeTo(sp, referenceFile);
        } catch (IOException e) {
            logger.error("An exception occurred while comparing to reference file {}: {}", referenceFile, e);
            fail();
            return;
        }
    }

}
