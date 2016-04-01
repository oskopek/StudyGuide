package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import com.oskopek.studyguide.model.courses.Credits;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Default implementation of {@link CourseScraper} for the pages at
 * <a href="http://www.mff.cuni.cz/studium/bcmgr/">http://www.mff.cuni.cz/studium/bcmgr/</a>.
 */
public class MFFWebScraper implements CourseScraper {

    @Override
    public CourseRegistry scrapeCourses(String url) throws IOException {
        CourseRegistry registry = new CourseRegistry();
        Document document = Jsoup.connect(url).get();
        Elements tables = document.select("table");
        for (int i = 0; i < 3; i++) {
            Element table = tables.get(i);
            boolean first = true; // skip header
            for (Element row : table.select("tr")) {
                if (first) {
                    first = false;
                    continue;
                }
                Elements tableData = row.select("td");
                String id = tableData.get(0).text();
                String name = tableData.get(1).text();
                Credits credits = Credits.valueOf(Integer.parseInt(tableData.get(3).text()));
                Course course = new Course(id, name, name, Locale.forLanguageTag("cs"), credits,
                        new ArrayList<>(), new ArrayList<>()); // TODO teachers and required courses
                registry.putCourse(course);
            }
        }
        return registry;
    }
}
