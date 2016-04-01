package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.courses.CourseRegistry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Default implementation of {@link CourseScraper} for the pages at
 * <a href="http://www.mff.cuni.cz/studium/bcmgr/">http://www.mff.cuni.cz/studium/bcmgr/</a>.
 */
public class MFFWebScraper implements CourseScraper {

    private final String sisWebUrl = "https://is.cuni.cz/studium"; // TODO move this somewhere
    private SISWebScraper sisWebScraper = new SISWebScraper(sisWebUrl);

    @Override
    public CourseRegistry scrapeCourses(String url) throws IOException { // TODO generally, make this more stable
        CourseRegistry registry = new CourseRegistry();
        Document document = Jsoup.connect(url).get();
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
                registry.copyCoursesFrom(sisWebScraper.scrapeCourses(id));
            }
        }
        return registry;
    }
}
