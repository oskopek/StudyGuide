package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Reads MFF StudyPlans from their html description pages.
 */
public class MffHtmlStudyPlanImporter implements DataReader {

    /**
     * Create an instance.
     */
    public MffHtmlStudyPlanImporter() {
        // intentionally empty
    }

    /**
     * Parses the HTML content of the description page into a usable {@link DefaultStudyPlan} model.
     *
     * @param stream the stream to read from
     * @return an initialized DefaultStudyPlan with constraints
     * @throws IOException if an error occured while parsing
     */
    private static DefaultStudyPlan parseDefaultStudyPlan(InputStream stream) throws IOException {
        // TODO html study plan importer http://www.mff.cuni.cz/studium/bcmgr/ok/ib3a21.htm
        return null;
    }

    @Override
    public StudyPlan readFrom(String fileName) throws IOException, IllegalArgumentException {
        if (fileName == null) {
            throw new IllegalArgumentException("FileName is null.");
        }
        return readFrom(Files.newInputStream(Paths.get(fileName)));
    }

    @Override
    public StudyPlan readFrom(InputStream inputStream) throws IOException, IllegalArgumentException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream is null.");
        }
        return parseDefaultStudyPlan(inputStream);
    }
}
