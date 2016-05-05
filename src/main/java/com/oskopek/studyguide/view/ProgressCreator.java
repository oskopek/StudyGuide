package com.oskopek.studyguide.view;

import com.oskopek.studyguide.persistence.ProgressObservable;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.TimerTask;

/**
 * Utility class for creating and displaying blocking progress pop-ups.
 */
public final class ProgressCreator {

    /**
     * Private default constructor to forbid instantiation.
     */
    private ProgressCreator() {
        // intentionally empty
    }

    /**
     * A util method to display a blocking progress pop-up with the given parameters.
     */
    public static <ProgressObservable_ extends ProgressObservable> Stage showProgress(
            ProgressObservable_ progressObservable, String message) {
        Stage dialogStage = new Stage();
        ProgressBar progressBar = new ProgressBar();

        dialogStage.initStyle(StageStyle.DECORATED);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        final Label label = new Label();
        label.setText(message);

        progressBar.progressProperty().bind(progressObservable.progressProperty());

        final VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, progressBar);

        Scene scene = new Scene(vBox);
        dialogStage.setScene(scene);
        dialogStage.toFront();
        dialogStage.setOnCloseRequest(event -> event.consume()); // prevent closing
        dialogStage.show();
        return dialogStage;
    }
}
