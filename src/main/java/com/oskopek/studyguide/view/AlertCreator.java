package com.oskopek.studyguide.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Utility class for creating and displaying alert pop-up windows.
 */
public final class AlertCreator {

    /**
     * Private default constructor to forbid instantiation.
     */
    private AlertCreator() {
        // intentionally empty
    }

    private static void showAlertInternal(Alert.AlertType alertType, String message, ButtonType... buttonTypes) {
        Alert alert = new Alert(alertType, "", buttonTypes);
        alert.getDialogPane().setContent(new Label(message));
        alert.showAndWait();
    }

    /**
     * A util method to display an {@link Alert} with the given parameters.
     *
     * @param alertType the type of the alert
     * @param message the message to display
     * @param buttonTypes the buttons to show
     * @see Alert
     */
    public static void showAlert(Alert.AlertType alertType, String message, ButtonType... buttonTypes) {
        Platform.runLater(() -> showAlertInternal(alertType, message, buttonTypes));
    }

    /**
     * A util method to display an {@link Alert} with the given parameters.
     *
     * @param alertType the type of the alert
     * @param message the message to display
     * @param buttonTypes the buttons to show
     * @see Alert
     */
    public static void showAlertAndExit(Alert.AlertType alertType, String message, ButtonType... buttonTypes) {
        Platform.runLater(() -> {
            showAlertInternal(alertType, message, buttonTypes);
            System.exit(0);
        });
    }

    /**
     * Show a GUI alert when an exception loading FXMLs occurs.
     * Throws IllegalStateException throws a wrapper exception around {@code e} in the javafx thread
     *
     * @param messages the message to show in the alert
     * @param e the exception to throw
     */
    public static void handleLoadLayoutError(ResourceBundle messages, IOException e) {
        Platform.runLater(() -> {
            throw new IllegalStateException(messages.getString("error.cannotLoadLayout"), e);
        });
        AlertCreator.showAlertAndExit(Alert.AlertType.ERROR,
                messages.getString("error.cannotLoadLayout") + ":\n\n" + e.getLocalizedMessage(), ButtonType.CLOSE);
    }

}
