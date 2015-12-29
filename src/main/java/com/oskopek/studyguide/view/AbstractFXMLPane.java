package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.AbstractController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

/**
 * Abstraction of a pane to be loaded from an FXML file. Subclasses should specify the return type.
 */
public abstract class AbstractFXMLPane {

    /**
     * A static instance of {@link StudyGuideResourceBundle} for translating strings in the view.
     */
    public static final StudyGuideResourceBundle messages = new StudyGuideResourceBundle();

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
        loader.setResources(AbstractFXMLPane.messages);
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
        Alert alert = new Alert(Alert.AlertType.ERROR, AbstractFXMLPane.messages.getString("error.cannotLoadLayout"),
                ButtonType.CLOSE);
        alert.showAndWait();
        throw new IllegalStateException(AbstractFXMLPane.messages.getString("error.cannotLoadLayout"), e);
    }

}
