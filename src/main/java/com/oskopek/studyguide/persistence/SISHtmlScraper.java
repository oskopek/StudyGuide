package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import com.oskopek.studyguide.model.courses.Credits;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Scrapes a SIS instance (f.e. <a href="https://is.cuni.cz/studium">https://is.cuni.cz/studium</a>).
 * <strong>Works only in Czech locale!</strong>
 */
public class SISHtmlScraper implements ProgressObservable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DoubleProperty progressProperty = new SimpleDoubleProperty(-1d);
    private String sisUrl;

    /**
     * Default constructor.
     *
     * @param sisUrl the base url of the SIS instance (without a trailing slash)
     */
    public SISHtmlScraper(String sisUrl) {
        if (sisUrl == null) {
            throw new IllegalArgumentException("SIS url cannot be null.");
        }
        this.sisUrl = sisUrl;
    }

    /**
     * Scrapes the given SIS instance for a specific course, adding all required courses to the registry.
     *
     * @param registry a {@link CourseRegistry} not containing this course
     * @param courseId the subject id to search in SIS
     * @return the course of the given id we scraped, non-null
     * @throws IOException if an error occurs while downloading the pages to scrape
     */
    public Course scrapeCourse(CourseRegistry registry, String courseId) throws IOException {
        if (courseId == null) {
            throw new IllegalArgumentException("Course id cannot be null.");
        }
        Course course = registry.getCourse(courseId);
        if (course != null) {
            return course;
        }
        logger.debug("Scraping from SIS: {}", courseId);
        String urlString = sisUrl + "/predmety/index.php?do=predmet&kod=" + courseId;

        InputStream is;
        String encoding;
        if (urlString.startsWith("file://")) { // TODO hack
            urlString = urlString.substring(7);
            is = Files.newInputStream(Paths.get(urlString));
            encoding = "utf-8";
        } else {
            URL url = new URL(urlString); // http://www.dmurph.com/2011/01/java-uri-encoder/
            URLConnection urlConnection = url.openConnection();
            is = urlConnection.getInputStream();
            encoding = urlConnection.getContentEncoding();
        }
        return scrapeCourse(registry, is, encoding, courseId);
    }

    /**
     * The actual implementation of the scraper. Adds the course and all courses required for this course to the
     * registry.
     *
     * @param registry a {@link CourseRegistry} not containing this course
     * @param is the stream from which to parse the SIS course html
     * @param encoding the encoding of html in the stream
     * @param courseId the id of the course being parsed
     * @return the course of the given id we scraped, non-null
     * @throws IOException if an error occurs during reading the input stream
     */
    private Course scrapeCourse(CourseRegistry registry, InputStream is, String encoding, String courseId)
            throws IOException {
        Document document = Jsoup.parse(is, encoding, ""); // we do not need a base url

        String localizedName = document.select("div.form_div_title").text();
        localizedName = localizedName.substring(0, localizedName.lastIndexOf("-")).trim();

        Elements tab2s = document.select("table.tab2");
        // skip table 0
        Elements table1 = tab2s.get(1).select("tr");
        String name = table1.get(0).select("td").first().text();
        Credits credits = Credits.valueOf(Integer.parseInt(table1.get(5).select("td").first().text()));

        Elements table2 = tab2s.get(2).select("tr");
        List<String> teacherList = new ArrayList<>();
        if (!table2.isEmpty()) {
            teacherList = table2.get(0).select("td").first().select("a.link3").stream().map(Element::text)
                    .collect(Collectors.toList());
        }

        CourseRegistry prereqs = new CourseRegistry();
        CourseRegistry coreqs = new CourseRegistry();
        for (Element tableRow : table2) { // TODO check for and fail on circular dependencies
            String headerText = tableRow.select("th").first().text().toLowerCase();
            CourseRegistry addTo;
            if (headerText.contains("korekvizity")) {
                addTo = coreqs;
            } else if (headerText.contains("prerekvizity")) {
                addTo = prereqs;
            } else {
                continue;
            }
            for (Element link : tableRow.select("td").first().select("a.link3")) {
                String id = link.text();
                Course dependency;
                try {
                    dependency = scrapeCourse(registry, id);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to parse required course: " + id + " of course " + courseId,
                            e);
                }
                addTo.putCourseSimple(dependency);
            }
        }

        Course course = new Course(courseId, name, localizedName, Locale.forLanguageTag("cs"), credits, teacherList,
                new ArrayList<>(prereqs.courseMapValues()), new ArrayList<>(coreqs.courseMapValues()));
        registry.putCourse(course);
        return course;
    }

    @Override
    public DoubleProperty progressProperty() {
        return progressProperty;
    }
}
