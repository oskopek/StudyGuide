package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.FindCoursesController;
import com.oskopek.studyguide.controller.SemesterController;
import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.weld.StartupStage;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * StudyGuide JavaFX main class.
 */
@Singleton
public class StudyGuideApplication extends Application {

    private Stage primaryStage;
    private ObjectProperty<StudyPlan> studyPlan;

    private WeldContainer container;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize Weld CDI
        container = new Weld().initialize();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("StudyGuide");
        this.primaryStage.getIcons().add(new Image(StudyGuideApplication.class.getResourceAsStream("logo_64x64.png")));

        container.event().select(StudyGuideApplication.class, new AnnotationLiteral<StartupStage>(){}).fire(this);
        studyPlan = new SimpleObjectProperty<>(new DefaultStudyPlan());
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

    public ObjectProperty<StudyPlan> studyPlanProperty() {
        return studyPlan;
    }

    /**
     * Set a new model instance.
     *
     * @param studyPlan the new model
     */
    public void setStudyPlan(StudyPlan studyPlan) { // TODO add handlers for this
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

//    /**
//     * Show a GUI alert when an exception loading FXMLs occurs.
//     *
//     * @param e the exception to throw
//     * @throws IllegalStateException throws a wrapper exception around {@code e}
//     */
//    private void handleLoadLayoutError(IOException e) throws IllegalStateException { // TODO fix me
//        AbstractFXMLPane.showAlert(Alert.AlertType.ERROR,
//                loader.getResources().getString("error.cannotLoadLayout"), ButtonType.CLOSE);
//        throw new IllegalStateException(loader.getResources().getString("error.cannotLoadLayout"), e);
//    }
//
//    /**
//     * A util method to display an {@link Alert} with the given parameters.
//     * @param alertType the type of the alert
//     * @param message the message to display
//     * @param buttonTypes the buttons to show
//     * @see Alert
//     */
//    public static void showAlert(Alert.AlertType alertType, String message, ButtonType... buttonTypes) {
//        Alert alert = new Alert(alertType, "", buttonTypes);
//        alert.getDialogPane().setContent(new Label(message));
//        alert.showAndWait();
//    }
}
