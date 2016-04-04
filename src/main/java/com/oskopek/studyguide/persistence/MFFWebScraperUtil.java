package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.StudyPlan;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * A quick util class to scrape a MFF html study plan.
 */
public final class MFFWebScraperUtil {

    private static final String mffIoiInfoUrl = "http://www.mff.cuni.cz/studium/bcmgr/ok/ib3a21.htm";
    private static final String sisWebUrl = "https://is.cuni.cz/studium";
    private static MFFHtmlScraper scraper = new MFFHtmlScraper(sisWebUrl);

    /**
     * An empty default private constructor.
     */
    private MFFWebScraperUtil() {
        // intentionally empty
    }

    /**
     * The main method to run the util. Will overwrite if an existing file with the same name exists.
     * Naming strategy: {@code test-current_time_in_millis.json}.
     *
     * @param args the command line arguments, ignored
     * @throws IOException if an exception during loading the page or writing the converted json happens
     */
    public static void main(String[] args) throws IOException {
            StudyPlan studyPlan = scraper.scrapeStudyPlan(mffIoiInfoUrl);
            System.out.println(Arrays.toString(studyPlan.getCourseRegistry().courseMapValues().toArray()));
            Path outputFile = Paths.get("test-" + System.currentTimeMillis() + ".json");
            System.out.println("Writing output to " + outputFile);
            new JsonDataReaderWriter().writeTo(studyPlan, outputFile.toString());
    }
}
