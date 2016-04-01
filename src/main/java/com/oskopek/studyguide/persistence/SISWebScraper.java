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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Scrapes a SIS instance (f.e. <a href="https://is.cuni.cz/studium">https://is.cuni.cz/studium</a>).
 * <strong>Works only in Czech locale!</strong>
 */
public class SISWebScraper implements CourseScraper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String sisWebUrl;

    /**
     * Default constructor.
     * @param sisWebUrl the base url of the SIS instance (without a trailing slash)
     */
    public SISWebScraper(String sisWebUrl) {
        this.sisWebUrl = sisWebUrl;
    }

    /**
     * Scrapes the given SIS instance for a specific course, returning all required courses too.
     * @param url the subject id to search in SIS
     * @return a {@link CourseRegistry} containing this course and all required courses
     * @throws IOException if an error occurs while downloading the pages to scrape
     */
    @Override
    public CourseRegistry scrapeCourses(String url) throws IOException {
        Document document = Jsoup.connect(sisWebUrl + "/predmety/index.php?do=predmet&kod=" + url).get();
        logger.debug("Scraping from SIS: {}", url); // TODO progress reporting action

        String id = url;
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
                    .stream().map(Element::text).map((String courseId) -> {
                        try {
                            return scrapeCourses(courseId);
                        } catch (IOException e) {
                            throw  new IllegalStateException("Failed to parse required course: " + courseId, e);
                        }
                    }).collect(Collectors.toList());
            for (CourseRegistry partialRegistry : partialRegistryList) {
                registry.copyCoursesFrom(partialRegistry);
            }
        }

        Course course = new Course(id, name, localizedName, Locale.forLanguageTag("cs"), credits, teacherList,
                new ArrayList<>(registry.courseMapValues()));
        registry.putCourse(course);
        return registry;
    }
}
