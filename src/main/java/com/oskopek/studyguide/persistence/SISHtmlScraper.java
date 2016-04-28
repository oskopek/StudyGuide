package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import com.oskopek.studyguide.model.courses.Credits;
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
public class SISHtmlScraper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String sisUrl;

    /**
     * Default constructor.
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
     * @param registry a {@link CourseRegistry} not containing this course
     * @param courseId the subject id to search in SIS
     * @throws IOException if an error occurs while downloading the pages to scrape
     */
    public void scrapeCourses(CourseRegistry registry, String courseId) throws IOException {
        if (courseId == null) {
            throw new IllegalArgumentException("Course id cannot be null.");
        } else if (registry.getCourse(courseId) != null) {
            return;
        }
        logger.debug("Scraping from SIS: {}", courseId); // TODO OPTIONAL progress reporting action
        String urlString = sisUrl + "/predmety/index.php?do=predmet&kod=" + courseId;

        InputStream is;
        String encoding;
        if (urlString.startsWith("file://")) { // TODO OPTIONAL hack
            urlString = urlString.substring(7);
            is = Files.newInputStream(Paths.get(urlString));
            encoding = "utf-8"; // TODO OPTIONAL UTF-8 is wrong
        } else {
            URL url = new URL(urlString); // http://www.dmurph.com/2011/01/java-uri-encoder/
            URLConnection urlConnection = url.openConnection();
            is = urlConnection.getInputStream();
            encoding = urlConnection.getContentEncoding();
        }
        scrapeCourses(registry, is, encoding, courseId);
    }

    /**
     * The actual implementation of the scraper. Adds the course and all courses required for this course to the
     * registry.
     *
     * @param registry a {@link CourseRegistry} not containing this course
     * @param is the stream from which to parse the SIS course html
     * @param encoding the encoding of html in the stream
     * @param courseId the id of the course being parsed
     * @throws IOException if an error occurs during reading the input stream
     */
    private void scrapeCourses(CourseRegistry registry, InputStream is, String encoding, String courseId)
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
            teacherList = table2.get(0).select("td").first().select("a.link3")
                    .stream().map(Element::text).collect(Collectors.toList());
        }

        // TODO correctly distinguish prereqs and coreqs
        CourseRegistry dependencies = new CourseRegistry();
        for (Element tableRow : table2) {
            String headerText = tableRow.select("th").first().text().toLowerCase();
            if (!headerText.contains("korekvizity") && !headerText.contains("prerekvizity")) {
                continue;
            }
            // TODO handle "nebo" conjunction correctly (i.e. not as an AND)
            // TODO handle circular dependencies
            for (Element link : tableRow.select("td").first().select("a.link3")) {
                String id = link.text();
                try {
                    scrapeCourses(dependencies, id);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to parse required course: " + id
                            + " of course " + courseId, e);
                }
            }
        }

        Course course = new Course(courseId, name, localizedName, Locale.forLanguageTag("cs"), credits, teacherList,
                new ArrayList<>(dependencies.courseMapValues()));
        registry.putCourse(course);
    }
}
