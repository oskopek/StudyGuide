package com.oskopek.studyguide.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * StudyGuide JavaFX main class.
 */
public class StudyGuideApplication extends Application {

    private Stage primaryStage;
    private VBox rootLayout;
    private BorderPane rootBorderPane;

    private final StudyGuideResourceBundle messages = new StudyGuideResourceBundle();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("StudyGuide");
        this.primaryStage.getIcons().add(new Image(StudyGuideApplication.class.getResourceAsStream("logo_64x64.png")));

        initRootLayout();
        showSemesterPane();
        showStudyPane();
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StudyGuideApplication.class.getResource("RootLayout.fxml"));
        loader.setResources(messages);
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            handleLoadLayoutError(e);
            return;
        }
        AnchorPane rootAnchorPane = (AnchorPane) rootLayout.getChildren().get(1);
        rootBorderPane = (BorderPane) rootAnchorPane.getChildren().get(0);

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Adds the semester pane to the root pane.
     */
    private void showSemesterPane() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StudyGuideApplication.class.getResource("SemesterPane.fxml"));
        loader.setResources(messages);
        BorderPane semesterPane;
        try {
             semesterPane = loader.load();
        } catch (IOException e) {
            handleLoadLayoutError(e);
            return;
        }
        rootBorderPane.setCenter(semesterPane);
    }

    /**
     * Adds the study pane to the root pane.
     */
    private void showStudyPane() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StudyGuideApplication.class.getResource("StudyPane.fxml"));
        loader.setResources(messages);
        VBox studyPane;
        try {
            studyPane = loader.load();
        } catch (IOException e) {
            handleLoadLayoutError(e);
            return;
        }
        rootBorderPane.setRight(studyPane);
    }

    /**
     * Show a GUI alert when an exception loading FXMLs occurs.
     *
     * @param e the exception to throw
     * @throws IllegalStateException throws a wrapper exception around {@code e}
     */
    private void handleLoadLayoutError(IOException e) throws IllegalStateException {
        Alert alert = new Alert(Alert.AlertType.ERROR, messages.getString("error.cannotLoadLayout"), ButtonType.CLOSE);
        alert.showAndWait();
        throw new IllegalStateException(messages.getString("error.cannotLoadLayout"), e);
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
