package com.oskopek.studyguide.persistence;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Probably deserves more thorough testing.
 */
public class JsonDataReaderWriterTest {

    private JsonDataReaderWriter jsonDataReaderWriter;
    private Path jsonPath;
    private StudyPlan plan;

    @Before
    public void setUp() throws IOException {
        jsonDataReaderWriter = new JsonDataReaderWriter();
        jsonPath = Files.createTempFile("tmpPlan", ".json");
        Files.copy(Paths.get("src/test/resources/com/oskopek/studyguide/persistence/json_empty.json"), jsonPath,
                StandardCopyOption.REPLACE_EXISTING);
        plan = new DefaultStudyPlan();
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(jsonPath);
    }

    @Test
    public void writeToString() throws IOException {
        jsonDataReaderWriter.writeTo(plan, jsonPath.toString());
        assertEquals("{", Files.readAllLines(jsonPath).get(0));
    }

    @Test
    public void writeToStream() throws IOException {
        jsonDataReaderWriter.writeTo(plan, Files.newOutputStream(jsonPath));
        assertEquals("{", Files.readAllLines(jsonPath).get(0));
    }

    @Test
    public void readFromString() throws IOException {
        StudyPlan plan = jsonDataReaderWriter.readFrom(jsonPath.toString());
        assertNotNull(plan);
        assertNotNull(plan.getSemesterPlan());
        assertEquals(0, plan.getSemesterPlan().getSemesterList().size());
    }

    @Test
    public void readFromStream() throws IOException {
        StudyPlan plan = jsonDataReaderWriter.readFrom(Files.newInputStream(jsonPath));
        assertNotNull(plan);
    }

    @Test
    public void readComplexInput() throws IOException {
        DefaultStudyPlan plan = (DefaultStudyPlan) jsonDataReaderWriter.readFrom(Files.newInputStream(
                Paths.get("src/test/resources/com/oskopek/studyguide/persistence/my_study.json")));
        assertNotNull(plan);
        assertNotNull(plan.getSemesterPlan());
        assertNotNull(plan.getSemesterPlan().getSemesterList());
        assertNotNull(plan.getConstraints());
        assertNotNull(plan.getCourseRegistry());
        assertNotNull(plan.getCourseRegistry().courseMapValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void readFromNullStream() throws Exception {
        jsonDataReaderWriter.readFrom((InputStream) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readFromNullFileName() throws Exception {
        jsonDataReaderWriter.readFrom((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeToNullStream() throws Exception {
        jsonDataReaderWriter.writeTo(plan, (OutputStream) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeToNullFileName() throws Exception {
        jsonDataReaderWriter.writeTo(plan, (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNullPlanToStream() throws Exception {
        jsonDataReaderWriter.writeTo(null, Files.newOutputStream(jsonPath));
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeNullPlanToFileName() throws Exception {
        jsonDataReaderWriter.writeTo(null, jsonPath.toString());
    }

}
