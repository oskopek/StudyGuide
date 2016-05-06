package com.oskopek.studyguide.view;

import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.weld.StartupStage;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Singleton;

/**
 * StudyGuide JavaFX main class.
 */
@Singleton
public class StudyGuideApplication extends Application {

    private final String logoResource = "logo_64x64.png";
    private final String logoResourceLarge = "logo_640x640.png";
    private Stage primaryStage;
    private ObjectProperty<StudyPlan> studyPlan = new SimpleObjectProperty<>();
    private WeldContainer container;

    /**
     * Main method.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage initStage) throws Exception {
        primaryStage = new Stage(StageStyle.DECORATED);
        Task<ObservableValue<Stage>> mainStageTask = new Task<ObservableValue<Stage>>() {
            @Override
            protected ObservableValue<Stage> call() throws Exception {
                container = new Weld().initialize(); // Initialize Weld CDI
                primaryStage.setTitle("StudyGuide");
                primaryStage.getIcons().add(new Image(StudyGuideApplication.class.getResourceAsStream(logoResource)));
                container.event().select(Stage.class, new AnnotationLiteral<StartupStage>() {
                }).fire(primaryStage);
                return new ReadOnlyObjectWrapper<>(primaryStage);
            }
        };
        mainStageTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                throw new IllegalStateException("Main stage loading failed.", newValue);
            });
        });
        showSplashScreen(initStage, mainStageTask);
    }

    /**
     * Helper method to construct a splash screen and display it while preparing the main stage for the application.
     *
     * @param initStage the initial stage to display the splash screen on
     * @param mainStageTask the task that loads, constructs and displays the main app
     */
    private void showSplashScreen(Stage initStage, Task<ObservableValue<Stage>> mainStageTask) {
        int SPLASH_WIDTH = 640;
        int SPLASH_HEIGHT = 640;
        Pane splashLayout = new VBox();
        ImageView splash = new ImageView(new Image(getClass().getResourceAsStream(logoResourceLarge)));
        splashLayout.getChildren().add(splash);
        mainStageTask.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(javafx.util.Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();
            }
        });
        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.show();
        new Thread(mainStageTask).start();
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
     * Set a new model instance.
     *
     * @param studyPlan the new model
     */
    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan.setValue(studyPlan);
    }

    /**
     * The JavaFX property for {@link #getStudyPlan()}.
     *
     * @return the study plan property
     */
    public ObjectProperty<StudyPlan> studyPlanProperty() {
        return studyPlan;
    }
}
