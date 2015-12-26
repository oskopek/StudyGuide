package com.oskopek.studyguide.view;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
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
    private StudyPlan studyPlan;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("StudyGuide");
        this.primaryStage.getIcons().add(new Image(StudyGuideApplication.class.getResourceAsStream("logo_64x64.png")));

        studyPlan = new DefaultStudyPlan();
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        VBox rootLayout = (VBox) new RootLayoutPane().load(this);
        AnchorPane rootAnchorPane = (AnchorPane) rootLayout.getChildren().get(1);
        BorderPane rootBorderPane = (BorderPane) rootAnchorPane.getChildren().get(0);

        BorderPane semesterPane = (BorderPane) new SemesterPane().load(this);
        rootBorderPane.setCenter(semesterPane);

        VBox studyPane = (VBox) new StudyPane().load(this);
        rootBorderPane.setRight(studyPane);

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public StudyPlan getStudyPlan() {
        return studyPlan;
    }

    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
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
