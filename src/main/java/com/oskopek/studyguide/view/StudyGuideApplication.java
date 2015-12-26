package com.oskopek.studyguide.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * StudyGuide JavaFX main class.
 */
public class StudyGuideApplication extends Application {

    private Stage primaryStage;
    private VBox rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("StudyGuide");
        this.primaryStage.getIcons().add(new Image(StudyGuideApplication.class.getResourceAsStream("logo_64x64.png")));

        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        rootLayout = (VBox) new RootLayoutPane().load();
        AnchorPane rootAnchorPane = (AnchorPane) rootLayout.getChildren().get(1);
        BorderPane rootBorderPane = (BorderPane) rootAnchorPane.getChildren().get(0);

        BorderPane semesterPane = (BorderPane) new SemesterPane().load();
        rootBorderPane.setCenter(semesterPane);

        VBox studyPane = (VBox) new StudyPane().load();
        rootBorderPane.setRight(studyPane);

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
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
