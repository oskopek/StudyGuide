package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
public class MFFHtmlScraper implements DataReader, ProgressObservable {

    private SISHtmlScraper sisHtmlScraper;

    private DoubleProperty progressProperty = new SimpleDoubleProperty();

    /**
     * Default constructor.
     * @param sisUrl the url of a SIS instance
     */
    public MFFHtmlScraper(String sisUrl) {
        if (sisUrl == null) {
            throw new IllegalArgumentException("SIS Url cannot be null.");
        }
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
        CourseRegistry registry = new CourseRegistry();
        Document document = Jsoup.parse(stream, encoding, ""); // do not need to resolve relative links
        Elements tables = document.select("table");
        for (Element table : tables) {
            boolean first = true; // skip header
            Elements rows = table.select("tr");
            int rowIndex = 0;
            for (Element row : rows) {
                progressProperty.set(rowIndex / (double) rows.size());
                if (first) {
                    first = false;
                    continue;
                }
                Elements tableData = row.select("td");
                String id = tableData.first().text().replaceAll("[^a-zA-Z0-9]+", "");
                if (!id.isEmpty()) { // skip empty lines
                    sisHtmlScraper.scrapeCourse(registry, id);
                }
                rowIndex++;
            }
        }
        progressProperty.set(1d);
        DefaultStudyPlan studyPlan = new DefaultStudyPlan();
        studyPlan.courseRegistryProperty().setValue(registry);
        return studyPlan;
    }

    /**
     * Scrape the study plan from a local html file.
     * @param path the path to load the file from
     * @return the scraped study plan
     * @throws IOException if an exception occurs while reading the file
     */
    public StudyPlan scrapeStudyPlan(Path path) throws IOException {
        if (path == null || !Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Path " + path + " is null or not a regular file.");
        }
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
        if (url == null) {
            throw new IllegalArgumentException("Url to scrape cannot be null.");
        }
        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            InputStream is = connection.getInputStream();
            StudyPlan studyPlan = scrapeStudyPlan(is, connection.getContentEncoding());
            is.close();
            return studyPlan;
        } catch (IOException e) {
            throw new IOException("Failed to scrape study plan from: " + url, e);
        }
    }

    @Override
    public StudyPlan readFrom(String fileName) throws IOException, IllegalArgumentException {
        if (fileName == null) {
            throw new IllegalArgumentException("File name cannot be null.");
        }
        return scrapeStudyPlan(Paths.get(fileName));
    }

    @Override
    public StudyPlan readFrom(InputStream inputStream) throws IOException, IllegalArgumentException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null.");
        }
        return scrapeStudyPlan(inputStream, "utf-8"); // TODO OPTIONAL UTF-8 is wrong
    }

    public DoubleProperty progressProperty() {
        return progressProperty;
    }
}
