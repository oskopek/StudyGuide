package com.oskopek.studyguide.view;

import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.weld.StartupStage;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Singleton;

/**
 * StudyGuide JavaFX main class.
 */
@Singleton
public class StudyGuideApplication extends Application {

    private Stage primaryStage;
    private ObjectProperty<StudyPlan> studyPlan = new SimpleObjectProperty<>();

    private WeldContainer container;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize Weld CDI
        container = new Weld().initialize();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("StudyGuide");
        this.primaryStage.getIcons().add(new Image(StudyGuideApplication.class.getResourceAsStream("logo_64x64.png")));

        container.event().select(Stage.class, new AnnotationLiteral<StartupStage>() { }).fire(primaryStage);
    }

    /**
     * Get the main {@link javafx.stage.Window} element of the app.
     *
     * @return non-null
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Get the current model instance.
     *
     * @return may be null (no study plan currently loaded)
     */
    public StudyPlan getStudyPlan() {
        return studyPlan.get();
    }

    /**
     * The JavaFX property for {@link #getStudyPlan()}.
     * @return the study plan property
     */
    public ObjectProperty<StudyPlan> studyPlanProperty() {
        return studyPlan;
    }

    /**
     * Set a new model instance.
     *
     * @param studyPlan the new model
     */
    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan.setValue(studyPlan);
    }

    /**
     * Main method.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
