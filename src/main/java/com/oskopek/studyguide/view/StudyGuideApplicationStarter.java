package com.oskopek.studyguide.view;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.weld.StartupStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;

/**
 * A CDI-enabled version of {@link StudyGuideApplication} that initializes the root layout.
 */
public class StudyGuideApplicationStarter {

    @Inject
    @Named("fxmlloader")
    private Instance<FXMLLoader> fxmlLoader;

    @Inject
    private StudyGuideApplication studyGuideApplication;

    /**
     * Initializes the root layout.
     * @param primaryStage the primaryStage delegated from the {@link javafx.application.Application} that calls us
     */
    private void initRootLayout(@Observes @StartupStage Stage primaryStage) {
        FXMLLoader fxmlLoader = this.fxmlLoader.get();
        VBox rootLayout = null;
        fxmlLoader.setLocation(getClass().getResource("RootLayoutPane.fxml"));
        try (InputStream is = getClass().getResourceAsStream("RootLayoutPane.fxml")) {
            rootLayout = fxmlLoader.load(is);
        } catch (IOException e) {
            AlertCreator.handleLoadLayoutError(fxmlLoader.getResources(), e);
        }
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
