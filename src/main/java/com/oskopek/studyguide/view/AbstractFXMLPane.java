package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.AbstractController;
import com.oskopek.studyguide.weld.MessagesProducer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Abstraction of a pane to be loaded from an FXML file. Subclasses should specify the return type.
 */
public abstract class AbstractFXMLPane {

    /**
     * An injected instance of {@link ResourceBundle} for translating strings in the view or constraints.
     * @see MessagesProducer#createMessagesResourceBundle()
     */
    @Inject
    protected ResourceBundle messages;

    protected AbstractController controller;
    protected StudyGuideApplication studyGuideApplication;

    /**
     * Loads a FXML resource from the filesystem: {@link #getFxmlResource()}.
     *
     * @param studyGuideApplication reference to the main application, non-null
     * @return non-null
     */
    public Node load(StudyGuideApplication studyGuideApplication) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(getFxmlResource()));
        loader.setResources(messages);
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            handleLoadLayoutError(e);
        }
        this.controller = loader.getController();
        this.studyGuideApplication = studyGuideApplication;
        return node;
    }

    /**
     * Name of this FXML resource.
     *
     * @return a non-null string representing a FXML resource
     */
    protected abstract String getFxmlResource();

    /**
     * Reference to the controller of this pane.
     *
     * @return the controller controlling this pane, non-null
     */
    public AbstractController getController() {
        return controller;
    }

    /**
     * Reference to the main app (and model).
     *
     * @return non-null
     */
    public StudyGuideApplication getStudyGuideApplication() {
        return studyGuideApplication;
    }

    /**
     * Show a GUI alert when an exception loading FXMLs occurs.
     *
     * @param e the exception to throw
     * @throws IllegalStateException throws a wrapper exception around {@code e}
     */
    private void handleLoadLayoutError(IOException e) throws IllegalStateException {
        AbstractFXMLPane.showAlert(Alert.AlertType.ERROR,
                messages.getString("error.cannotLoadLayout"), ButtonType.CLOSE);
        throw new IllegalStateException(messages.getString("error.cannotLoadLayout"), e);
    }

    /**
     * A util method to display an {@link Alert} with the given parameters.
     * @param alertType the type of the alert
     * @param message the message to display
     * @param buttonTypes the buttons to show
     * @see Alert
     */
    public static void showAlert(Alert.AlertType alertType, String message, ButtonType... buttonTypes) {
        Alert alert = new Alert(alertType, "", buttonTypes);
        alert.getDialogPane().setContent(new Label(message));
        alert.showAndWait();
    }

}
