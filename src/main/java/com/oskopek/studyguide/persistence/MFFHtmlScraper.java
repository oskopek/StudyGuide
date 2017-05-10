package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.constraint.*;
import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.SemesterPlan;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.constraints.Constraints;
import com.oskopek.studyguide.model.constraints.CourseGroup;
import com.oskopek.studyguide.model.courses.Course;
import com.oskopek.studyguide.model.courses.CourseRegistry;
import com.oskopek.studyguide.model.courses.Credits;
import com.oskopek.studyguide.weld.BeanManagerUtil;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An online HTML scraper for the pages at
 * <a href="http://www.mff.cuni.cz/studium/bcmgr/">http://www.mff.cuni.cz/studium/bcmgr/</a>.
 */
public class MFFHtmlScraper implements DataReader, ProgressObservable {

    private final DoubleProperty progressProperty = new SimpleDoubleProperty();
    private SISHtmlScraper sisHtmlScraper;

    /**
     * Default constructor.
     *
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
        DefaultStudyPlan studyPlan = new DefaultStudyPlan();
        Document document = Jsoup.parse(stream, encoding, ""); // do not need to resolve relative links
        scrapeContents(document, studyPlan);
        progressProperty.set(1d);
        return studyPlan;
    }

    /**
     * Scrapes the contents of the document, looking for
     * {@link com.oskopek.studyguide.constraint.CourseGroupConstraint}s,
     * {@link com.oskopek.studyguide.constraint.GlobalConstraint}s and {@link Course}s.
     *
     * @param document the document to scrape for courses and constraints
     * @param studyPlan the plan to fill with scraped information from the document
     * @throws IOException if an error during scraping occurs
     */
    private void scrapeContents(Document document, DefaultStudyPlan studyPlan) throws IOException {
        Constraints constraints = new Constraints();
        CourseRegistry registry = new CourseRegistry();
        SemesterPlan semesterPlan = studyPlan.getSemesterPlan();

        Elements groupHeaderElements = document.select("h4");
        for (Element header : groupHeaderElements) {
            String headerName = header.text();
            if (filterCompulsory(headerName)) {
                Element table = header.nextElementSibling();
                CourseRegistry compulsory = new CourseRegistry();
                scrapeCoursesFromTable(table, compulsory);
                // compulsory courses are transitive (their dependencies have to be compulsory), we can add all
                registry.putAllCourses(compulsory);
                CourseGroup group = new CourseGroup(registry.courseMapValues());
                CourseGroupFulfilledAllConstraint constraint = BeanManagerUtil
                        .createBeanInstance(CourseGroupFulfilledAllConstraint.class);
                constraint.setCourseGroup(group);
                constraint.setSemesterPlan(semesterPlan);
                constraints.getCourseGroupConstraintList().add(constraint);
            } else if (filterSemiCompulsory(headerName)) {
                Element desc = header.nextElementSibling();
                Credits neededCreditSum = filterNeededCreditSum(desc.text());
                Element table = desc.nextElementSibling();
                CourseRegistry semiCompulsory = new CourseRegistry();
                // semi-compulsory courses are NOT transitive, do not add their dependencies
                List<String> courseIds = scrapeCoursesFromTable(table, semiCompulsory);
                List<Course> courses = courseIds.stream().map(semiCompulsory::getCourse).collect(Collectors.toList());
                registry.putAllCourses(courses);
                CourseGroup group = new CourseGroup(courses);
                CourseGroupCreditsSumConstraint constraint = BeanManagerUtil
                        .createBeanInstance(CourseGroupCreditsSumConstraint.class);
                constraint.setCourseGroup(group);
                constraint.setSemesterPlan(semesterPlan);
                constraint.setTotalNeeded(neededCreditSum);
                constraints.getCourseGroupConstraintList().add(constraint);
            }
        }

        // parse all tables in document (including ones we skipped before)
        Elements tables = document.select("table");
        for (Element table : tables) {
            scrapeCoursesFromTable(table, registry);
        }

        // add global constraints we know to be of effect
        GlobalCourseRepeatedEnrollmentConstraint c1 = BeanManagerUtil
                .createBeanInstance(GlobalCourseRepeatedEnrollmentConstraint.class);
        c1.setMaxRepeatedEnrollment(2);
        c1.setSemesterPlan(semesterPlan);

        Optional<String> lastYear = document.select("h4").stream().map(Element::text)
                .filter(row -> row.contains("rok studia")).sorted((x, y) -> -x.compareTo(y)).findFirst();
        int lastYearInt = 2; // default for Mgr
        if (lastYear.isPresent()) {
            lastYearInt = lastYear.get().charAt(0) - '0';
        }
        GlobalCreditsSumConstraint c2 = BeanManagerUtil.createBeanInstance(GlobalCreditsSumConstraint.class);
        c2.setTotalNeeded(Credits.valueOf(60 * lastYearInt));
        c2.setSemesterPlan(semesterPlan);

        GlobalCourseMaxFulfilledConstraint c3 = BeanManagerUtil
                .createBeanInstance(GlobalCourseMaxFulfilledConstraint.class);
        c3.setMaxFulfilled(1);
        c3.setSemesterPlan(semesterPlan);

        GlobalLongStudyFeeConstraint c4 = BeanManagerUtil.createBeanInstance(GlobalLongStudyFeeConstraint.class);
        c4.setCurrency(Currency.getInstance("CZK"));
        c4.setFromSemester((lastYearInt + 1) * 2 + 1); // one more year + convert to semesters (counted from 1)
        c4.setFeePerSemester(BigDecimal.valueOf(
                22000L)); // http://www.cuni.cz/UK-917-version1-vyse_poplatku_na_uk_2016_2017.pdf MFF informatika
        c4.setSemesterPlan(semesterPlan);

        constraints.getGlobalConstraintList().addAll(c1, c2, c3, c4);

        for (int i = 1; i <= lastYearInt * 2; i++) {
            GlobalCreditsSumUntilSemesterConstraint su = BeanManagerUtil
                    .createBeanInstance(GlobalCreditsSumUntilSemesterConstraint.class);
            su.setTotalNeeded(Credits.valueOf(30 * i));
            su.setUntilSemester(i);
            su.setSemesterPlan(semesterPlan);
            constraints.getGlobalConstraintList().add(su);
        }

        studyPlan.courseRegistryProperty().set(registry);
        studyPlan.constraintsProperty().set(constraints);
    }

    // TODO rewrite to be generic where the filters are provided externally as callbacks

    /**
     * Check whether the given string is the header text of a compulsory {@link CourseGroup}.
     *
     * @param headerName the text in the header element
     * @return true if it is this is the header text of a compulsory course group
     */
    private boolean filterCompulsory(String headerName) {
        headerName = normalize(headerName);
        return headerName.startsWith("povinnepredmety");
    }

    /**
     * Check whether the given string is the header text of a semi-compulsory {@link CourseGroup}.
     *
     * @param headerName the text in the header element
     * @return true if it is this is the header text of a semi-compulsory course group
     */
    private boolean filterSemiCompulsory(String headerName) {
        headerName = normalize(headerName);
        return headerName.startsWith("povinnevolitelne");
    }

    /**
     * Deletes whitespace, removes diacritics, removes all non-ASCII characters and converts the string to lower
     * case in this order.
     *
     * @param string the string to normalize
     * @return the normalized string
     */
    private String normalize(String string) {
        return Normalizer.normalize(StringUtils.deleteWhitespace(string), Normalizer.Form.NFD)
                .replaceAll("[^\\x00-\\x7F]", "").toLowerCase();
    }

    /**
     * Filters the given string, looking for a token that could represent the needed credit sum needed to pass
     * the {@link CourseGroupCreditsSumConstraint}.
     *
     * @param description the description to filter
     * @return a credit instance representing the sum
     */
    private Credits filterNeededCreditSum(String description) {
        description = normalize(description);
        Matcher matcher = Pattern.compile("([1-9][0-9]*)kredit").matcher(description);
        if (matcher.find()) {
            String creditsValue = matcher.group(1);
            int value;
            try {
                value = Integer.parseInt(creditsValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Could not parse credit sum from: " + creditsValue, e);
            }
            return Credits.valueOf(value);
        }
        throw new IllegalArgumentException("Could not find credit sum in: " + description);
    }

    /**
     * Scrapes course IDs from a table element and fills the registry with the correctly parse courses.
     * Uses a {@link SISHtmlScraper}.
     *
     * @param table the table from which to scrape course IDs
     * @param registry the registry to fill with courses from the table
     * @return list of ids that were in the table
     * @throws IOException if an error during scraping occurs
     */
    private List<String> scrapeCoursesFromTable(Element table, CourseRegistry registry) throws IOException {
        List<String> ids = new ArrayList<>();
        Elements rows = table.select("tr");
        int rowIndex = 0;
        for (Element row : rows) {
            progressProperty.set(rowIndex / (double) rows.size());
            if (rowIndex == 0) {
                rowIndex++;
                continue; // skip header
            }
            Elements tableData = row.select("td");
            String id = tableData.first().text().replaceAll("[^a-zA-Z0-9]+", "");
            if (id.isEmpty()) { // skip empty lines
                continue;
            }
            ids.add(id);
            if (registry.getCourse(id) == null) { // skip existing courses
                sisHtmlScraper.scrapeCourse(registry, id);
            }
            rowIndex++;
        }
        return ids;
    }

    /**
     * Scrape the study plan from a local html file.
     *
     * @param path the path to load the file from
     * @return the scraped study plan
     * @throws IOException if an exception occurs while reading the file
     */
    public StudyPlan scrapeStudyPlan(Path path) throws IOException {
        if (path == null || !Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Path " + path + " is null or not a regular file.");
        }
        InputStream is = Files.newInputStream(path);
        StudyPlan studyPlan = scrapeStudyPlan(is, "utf-8");
        is.close();
        return studyPlan;
    }

    /**
     * Scrape the resource for a complete study plan, with constraints.
     *
     * @param url the url to scrape from
     * @return an initialized study plan
     * @throws IOException if an exception occurs while loading the resource
     */
    public StudyPlan scrapeStudyPlan(String url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("Url to scrape cannot be null.");
        }
        try {
            URL urlObj, base, next;
            HttpURLConnection connection;
            String location;

            while (true) { // handle redirects: http://stackoverflow.com/a/26046079/2713162
                urlObj = new URL(url);
                connection = (HttpURLConnection) urlObj.openConnection();
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setInstanceFollowRedirects(false);
                switch (connection.getResponseCode()) {
                    case HttpURLConnection.HTTP_MOVED_PERM:
                    case HttpURLConnection.HTTP_MOVED_TEMP:
                        location = connection.getHeaderField("Location");
                        base = new URL(url);
                        next = new URL(base, location);  // deal with relative URLs
                        url = next.toExternalForm();
                        continue;
                }
                break;
            }

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
        return scrapeStudyPlan(inputStream, "utf-8");
    }

    @Override
    public DoubleProperty progressProperty() {
        return progressProperty;
    }
}
