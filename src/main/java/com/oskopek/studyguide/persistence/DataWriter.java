package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.StudyPlan;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Basic methods for writing a {@link com.oskopek.studyguide.model.StudyPlan} to persistent data storage.
 */
public interface DataWriter {

    /**
     * Writes a {@link StudyPlan} to a file in the filesystem.
     *
     * @param plan     the StudyPlan to persist
     * @param fileName the file to write to
     * @throws IOException              if something writing the plan failed
     * @throws IllegalArgumentException if plan or fileName is null
     * @see DataReader#readFrom(String)
     */
    void writeTo(StudyPlan plan, String fileName) throws IOException, IllegalArgumentException;

    /**
     * Writes a {@link StudyPlan} to an {@link OutputStream}.
     *
     * @param plan         the StudyPlan to persist
     * @param outputStream the stream to write to
     * @throws IOException              if something writing the plan failed
     * @throws IllegalArgumentException if plan or outputStream is null
     * @see DataReader#readFrom(java.io.InputStream)
     */
    void writeTo(StudyPlan plan, OutputStream outputStream) throws IOException, IllegalArgumentException;

}
