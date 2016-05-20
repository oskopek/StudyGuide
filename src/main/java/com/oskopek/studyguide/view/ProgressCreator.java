package com.oskopek.studyguide.view;

import com.oskopek.studyguide.persistence.ProgressObservable;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
     *
     * @param progressObservable the observable object that gives us the progress
     * @param message the message to display while waiting
     * @return the stage of the opened dialog with the progress bar
     */
    public static Stage showProgress(ProgressObservable progressObservable, String message) {
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
        dialogStage.setOnCloseRequest(Event::consume); // prevent closing of the dialog window
        dialogStage.show();
        return dialogStage;
    }
}
