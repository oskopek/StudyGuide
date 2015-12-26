package com.oskopek.studyguide.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

/**
 * Abstraction of a pane to be loaded from an FXML file. Subclasses should specify the return type.
 */
public abstract class AbstractFXMLPane {

    protected static final StudyGuideResourceBundle messages = new StudyGuideResourceBundle();

    /**
     * Loads a FXML resource from the filesystem: {@link #getFxmlResource()}.
     *
     * @return non-null
     */
    public Node load() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(getFxmlResource()));
        loader.setResources(AbstractFXMLPane.messages);
        try {
            return loader.load();
        } catch (IOException e) {
            handleLoadLayoutError(e);
            return null; // never occurs
        }
    }

    /**
     * Name of this FXML resource.
     *
     * @return a non-null string representing a FXML resource
     */
    protected abstract String getFxmlResource();

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
