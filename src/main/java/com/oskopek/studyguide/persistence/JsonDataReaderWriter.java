package com.oskopek.studyguide.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Reads and writes the {@link com.oskopek.studyguide.model.StudyPlan} to a JSON formatted file.
 *
 * Specifics of the format: TODO docs format spec
 */
public class JsonDataReaderWriter implements DataReader, DataWriter {

    private Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    @Override
    public StudyPlan readFrom(String fileName) throws IOException, IllegalArgumentException {
        if (fileName == null) {
            throw new IllegalArgumentException("FileName is null.");
        }
        try {
            return gson.fromJson(
                    Files.lines(Paths.get(fileName), Charset.forName("UTF-8")).collect(Collectors.joining()),
                    DefaultStudyPlan.class);
        } catch (IOException | JsonSyntaxException e) {
            throw new IOException("Failed to read StudyPlan from file (" + fileName + ").", e);
        }
    }

    @Override
    public StudyPlan readFrom(InputStream inputStream) throws IOException, IllegalArgumentException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream is null.");
        }
        try {
            return gson.fromJson(new BufferedReader(new InputStreamReader(inputStream)), DefaultStudyPlan.class);
        } catch (JsonSyntaxException e) {
            throw new IOException("Failed to read StudyPlan from stream.", e);
        }
    }

    @Override
    public void writeTo(StudyPlan plan, String fileName) throws IOException, IllegalArgumentException {
        if (fileName == null) {
            throw new IllegalArgumentException("FileName is null.");
        } else if (plan == null) {
            throw new IllegalArgumentException("Plan is null.");
        }
        try {
            writeTo(plan, Files.newOutputStream(Paths.get(fileName)));
        } catch (IOException | JsonSyntaxException e) {
            throw new IOException("Failed to write StudyPlan to file (" + fileName + ").", e);
        }
    }

    @Override
    public void writeTo(StudyPlan plan, OutputStream outputStream) throws IOException, IllegalArgumentException {
        if (outputStream == null) {
            throw new IllegalArgumentException("OutputStream is null.");
        } else if (plan == null) {
            throw new IllegalArgumentException("Plan is null.");
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            writer.write(gson.toJson(plan));
            writer.write('\n');
        } catch (IOException | JsonSyntaxException e) {
            throw new IOException("Failed to write StudyPlan to stream.", e);
        }
    }
}
