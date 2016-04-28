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
     * Scrapes the given SIS instance for a specific course, returning all required courses too.
     * @param courseId the subject id to search in SIS
     * @return a {@link CourseRegistry} containing this course and all required courses
     * @throws IOException if an error occurs while downloading the pages to scrape
     */
    public CourseRegistry scrapeCourses(String courseId) throws IOException {
        if (courseId == null) {
            throw new IllegalArgumentException("Course id cannot be null.");
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
        return scrapeCourses(is, encoding, courseId);
    }

    /**
     * The actual implementation of the scraper.
     *
     * @param is the stream from which to parse the SIS course html
     * @param encoding the encoding of html in the stream
     * @param courseId the id of the course being parsed TODO redundant (find it in the html)
     * @return a registry containing the course and all courses required for this course
     * @throws IOException if an error occurs during reading the input stream
     */
    private CourseRegistry scrapeCourses(InputStream is, String encoding, String courseId) throws IOException {
        Document document = Jsoup.parse(is, encoding, ""); // we do not need a base url

        String localizedName = document.select("div.form_div_title").text();
        localizedName = localizedName.substring(0, localizedName.lastIndexOf("-"));

        Elements tab2s = document.select("table.tab2");
        // skip table 0
        Elements table1 = tab2s.get(1).select("tr");
        String name = table1.get(0).select("td").first().text();
        Credits credits = Credits.valueOf(Integer.parseInt(table1.get(5).select("td").first().text()));

        Elements table2 = tab2s.get(2).select("tr");
        List<String> teacherList = table2.get(0).select("td").first().select("a.link3")
                .stream().map(Element::text).collect(Collectors.toList());

        // TODO correctly distinguish prereqs and coreqs
        CourseRegistry registry = new CourseRegistry();
        for (Element tableRow : table2) {
            String headerText = tableRow.select("th").first().text().toLowerCase();
            if (!headerText.contains("korekvizity") && !headerText.contains("prerekvizity")) {
                continue;
            }
            // TODO handle "nebo" conjunction correctly (i.e. not as an AND)
            List<CourseRegistry> partialRegistryList = tableRow.select("td").first().select("a.link3")
                    .stream().map(Element::text).map((String id) -> {
                        try {
                            return scrapeCourses(id);
                        } catch (IOException e) {
                            throw new IllegalStateException("Failed to parse required course: " + id
                                    + " of course " + courseId, e);
                        }
                    }).collect(Collectors.toList());
            for (CourseRegistry partialRegistry : partialRegistryList) {
                registry.copyCoursesFrom(partialRegistry);
            }
        }

        Course course = new Course(courseId, name, localizedName, Locale.forLanguageTag("cs"), credits, teacherList,
                new ArrayList<>(registry.courseMapValues()));
        registry.putCourse(course);
        return registry;
    }
}
