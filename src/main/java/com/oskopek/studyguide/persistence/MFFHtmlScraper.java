package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An online HTML scraper for the pages at
 * <a href="http://www.mff.cuni.cz/studium/bcmgr/">http://www.mff.cuni.cz/studium/bcmgr/</a>.
 */
public class MFFHtmlScraper implements DataReader { // TODO check for nulls, ...

    private SISHtmlScraper sisHtmlScraper;

    /**
     * Default constructor.
     * @param sisUrl the url of a SIS instance
     */
    public MFFHtmlScraper(String sisUrl) {
        sisHtmlScraper = new SISHtmlScraper(sisUrl);
    }

    /**
     * The actual implementation of the scraper.
     *
     * @param stream the stream to read html from
     * @param encoding encoding of html in the stream
     * @return the scraped study plan
     * @throws IOException if an exception occurs while reading the stream
     */
    private DefaultStudyPlan scrapeStudyPlan(InputStream stream, String encoding) throws IOException {
        // TODO generally, make this more stable, has to handle all default (Bc & Mgr) MFF study plans
        CourseRegistry registry = new CourseRegistry();
        Document document = Jsoup.parse(stream, encoding, ""); // do not need to resolve relative links
        Elements tables = document.select("table");
        // TODO gracefully handle other number of tables, but do not go to "recommended" plan
        for (int i = 0; i < 3; i++) {
            Element table = tables.get(i);
            boolean first = true; // skip header
            for (Element row : table.select("tr")) {
                if (first) {
                    first = false;
                    continue;
                }
                Elements tableData = row.select("td");
                String id = tableData.first().text();
                registry.copyCoursesFrom(sisHtmlScraper.scrapeCourses(id));
            }
        }
        DefaultStudyPlan studyPlan = new DefaultStudyPlan();
        studyPlan.courseRegistryProperty().setValue(registry); // TODO constraints
        return studyPlan;
    }

    /**
     * Scrape the study plan from a local html file.
     * @param path the path to load the file from
     * @return the scraped study plan
     * @throws IOException if an exception occurs while reading the file
     */
    public StudyPlan scrapeStudyPlan(Path path) throws IOException {
        InputStream is = Files.newInputStream(path);
        StudyPlan studyPlan = scrapeStudyPlan(is, "utf-8"); // TODO OPTIONAL UTF-8 is wrong
        is.close();
        return studyPlan;
    }

    /**
     * Scrape the resource for a complete study plan, with constraints.
     * @param url the url to scrape from
     * @return an initialized study plan
     * @throws IOException if an exception occurs while loading the resource
     */
    public StudyPlan scrapeStudyPlan(String url) throws IOException {
        URL urlObj = new URL(url);
        URLConnection connection = urlObj.openConnection();
        InputStream is = connection.getInputStream();
        StudyPlan studyPlan = scrapeStudyPlan(is, connection.getContentEncoding());
        is.close();
        return studyPlan;
    }

    @Override
    public StudyPlan readFrom(String fileName) throws IOException, IllegalArgumentException {
        return scrapeStudyPlan(Paths.get(fileName));
    }

    @Override
    public StudyPlan readFrom(InputStream inputStream) throws IOException, IllegalArgumentException {
        return scrapeStudyPlan(inputStream, "utf-8"); // TODO OPTIONAL UTF-8 is wrong
    }
}
