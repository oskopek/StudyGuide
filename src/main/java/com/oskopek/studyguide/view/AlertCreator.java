package com.oskopek.studyguide.view;

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

    /**
     * A util method to display an {@link Alert} with the given parameters.
     *
     * @param alertType   the type of the alert
     * @param message     the message to display
     * @param buttonTypes the buttons to show
     * @see Alert
     */
    public static void showAlert(Alert.AlertType alertType, String message, ButtonType... buttonTypes) {
        Alert alert = new Alert(alertType, "", buttonTypes);
        alert.getDialogPane().setContent(new Label(message));
        alert.showAndWait();
    }

    /**
     * Show a GUI alert when an exception loading FXMLs occurs.
     *
     * @param messages the message to show in the alert
     * @param e the exception to throw
     * @throws IllegalStateException throws a wrapper exception around {@code e}
     */
    public static void handleLoadLayoutError(ResourceBundle messages, IOException e) throws IllegalStateException {
        AlertCreator.showAlert(Alert.AlertType.ERROR,
                messages.getString("error.cannotLoadLayout"), ButtonType.CLOSE);
        throw new IllegalStateException(messages.getString("error.cannotLoadLayout"), e);
    }

}
