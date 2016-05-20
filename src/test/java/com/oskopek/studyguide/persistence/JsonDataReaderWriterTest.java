package com.oskopek.studyguide.persistence;

import com.google.common.eventbus.EventBus;
import com.oskopek.studyguide.constraint.CourseEnrollmentConstraint;
import com.oskopek.studyguide.constraint.GlobalConstraint;
import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.model.courses.Course;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Probably deserves more thorough testing.
 */
public class JsonDataReaderWriterTest {

    private JsonDataReaderWriter jsonDataReaderWriter;
    private Path jsonPath;
    private StudyPlan plan;
    private EventBus mockedEventBus;

    @Before
    public void setUp() throws IOException {
        mockedEventBus = Mockito.mock(EventBus.class);
        jsonDataReaderWriter = new JsonDataReaderWriter(null, mockedEventBus);

        jsonPath = Files.createTempFile("tmpPlan", ".json");
        Files.copy(Paths.get("src/test/resources/com/oskopek/studyguide/persistence/my_study.json"), jsonPath,
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
        assertEquals(6, plan.getSemesterPlan().getSemesterList().size());
    }

    @Test
    public void readFromStream() throws IOException {
        StudyPlan plan = jsonDataReaderWriter.readFrom(Files.newInputStream(jsonPath));
        assertNotNull(plan);
    }

    @Test
    public void readComplexInput() throws IOException {
        DefaultStudyPlan plan = (DefaultStudyPlan) jsonDataReaderWriter.readFrom(Files.newInputStream(jsonPath));
        assertNotNull(plan);
        assertNotNull(plan.getSemesterPlan());
        assertNotNull(plan.getSemesterPlan().getSemesterList());
        assertNotNull(plan.getConstraints());
        assertNotNull(plan.getCourseRegistry());
        assertNotNull(plan.getCourseRegistry().courseMapValues());
        assertNotNull(plan.getConstraints().getGlobalConstraintList());
        assertEquals(2, plan.getConstraints().getGlobalConstraintList().size());

        GlobalConstraint constraint = plan.getConstraints().getGlobalConstraintList().get(0);
        assertNotNull(constraint);
        assertNotNull(constraint.getSemesterPlan());
        assertTrue(constraint.getSemesterPlan() == plan.getSemesterPlan());

        CourseEnrollmentConstraint constraint2 = plan.getConstraints().getCourseEnrollmentConstraintList().get(0);
        assertNotNull(constraint2.getSemesterPlan());
        assertTrue(constraint2.getSemesterPlan() == plan.getSemesterPlan());
        // this will throw an exception if the eventBus is not injected correctly
        constraint.fireBrokenEvent("", (Course) null);
        verify(mockedEventBus, atLeastOnce()).post(anyObject());
        // this will throw an exception if the studyPlan is not injected correctly
        constraint.validate();
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
