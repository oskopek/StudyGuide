package com.oskopek.studyguide.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

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
     * Checks if the alert result is something "affirmative".
     * Specifically, returns true if the type is not null and is one of the following:
     * <ul>
     * <li>{@link ButtonType#APPLY}</li>
     * <li>{@link ButtonType#FINISH}</li>
     * <li>{@link ButtonType#OK}</li>
     * <li>{@link ButtonType#YES}</li>
     * </ul>
     *
     * @param type the type to verify
     * @return true iff all the conditions are met
     */
    private static boolean isAffirmative(ButtonType type) {
        return type != null && (type.equals(ButtonType.APPLY) || type.equals(ButtonType.FINISH) || type
                .equals(ButtonType.OK) || type.equals(ButtonType.YES));
    }

    /**
     * An internal method to display an {@link Alert} with the given parameters in the current thread.
     *
     * @param alertType the type of the alert
     * @param message the message to display
     * @param buttonTypes the buttons to show
     * @return true iff the result is accepted via {@link #isAffirmative(ButtonType)}
     */
    private static boolean showAlertInternal(Alert.AlertType alertType, String message, ButtonType... buttonTypes) {
        Alert alert = new Alert(alertType, "", buttonTypes);
        alert.getDialogPane().setContent(new Label(message));
        alert.showAndWait();
        return isAffirmative(alert.getResult());
    }

    /**
     * A util method to display an {@link Alert} with the given parameters in the UI thread.
     * Returns a boolean completable future that is true if the {@link #isAffirmative(ButtonType)} method returns true.
     *
     * @param alertType the type of the alert
     * @param message the message to display
     * @param buttonTypes the buttons to show
     * @return a completable future for appending tasks to be run after the alert is closed
     * @see Alert
     */
    public static CompletableFuture<Boolean> showAlert(Alert.AlertType alertType, String message,
            ButtonType... buttonTypes) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        Platform.runLater(() -> result.complete(showAlertInternal(alertType, message, buttonTypes)));
        return result;
    }

    /**
     * A util method to display an {@link Alert} with the given parameters in the UI thread, exiting the process
     * after the dialog is closed.
     *
     * @param alertType the type of the alert
     * @param message the message to display
     * @param buttonTypes the buttons to show
     * @see Alert
     */
    public static void showAlertAndExit(Alert.AlertType alertType, String message, ButtonType... buttonTypes) {
        showAlert(alertType, message, buttonTypes).thenRun(() -> System.exit(0));
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
