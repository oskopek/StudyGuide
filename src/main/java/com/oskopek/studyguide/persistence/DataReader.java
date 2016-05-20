package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.StudyPlan;

import java.io.IOException;
import java.io.InputStream;

/**
 * Basic methods for reading a {@link com.oskopek.studyguide.model.StudyPlan} from persistent data storage.
 */
public interface DataReader {

    /**
     * Reads a {@link StudyPlan} from a file in the filesystem.
     *
     * @param fileName the file from which to read
     * @return an initialized StudyPlan
     * @throws IOException if reading the plan failed
     * @throws IllegalArgumentException if fileName is null
     */
    StudyPlan readFrom(String fileName) throws IOException, IllegalArgumentException;

    /**
     * Reads a {@link StudyPlan} from the stream.
     *
     * @param inputStream the stream from which to read
     * @return an initialized StudyPlan
     * @throws IOException if reading the plan failed
     * @throws IllegalArgumentException if the inputStream is null
     */
    StudyPlan readFrom(InputStream inputStream) throws IOException, IllegalArgumentException;
}
