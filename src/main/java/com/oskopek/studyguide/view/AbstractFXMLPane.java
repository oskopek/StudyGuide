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

    public static final StudyGuideResourceBundle messages = new StudyGuideResourceBundle();
    protected Object controller;
    protected StudyGuideApplication studyGuideApplication;

    /**
     * Loads a FXML resource from the filesystem: {@link #getFxmlResource()}.
     *
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

    public Object getController() {
        return controller;
    }

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
