package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.persistence.DataReader;
import com.oskopek.studyguide.persistence.DataWriter;
import com.oskopek.studyguide.persistence.JsonDataReaderWriter;
import com.oskopek.studyguide.view.AbstractFXMLPane;
import com.oskopek.studyguide.view.RootLayoutPane;
import com.oskopek.studyguide.view.StudyGuideApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Handles all action in the menu bar of the main app.
 */
public class MenuBarController extends AbstractController<RootLayoutPane> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private File openedFile;

    private DataReader reader = new JsonDataReaderWriter();

    private DataWriter writer = new JsonDataReaderWriter();

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu fileMenu;

    @FXML
    private MenuItem newMenuItem;

    @FXML
    private MenuItem openMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private MenuItem saveAsMenuItem;

    @FXML
    private MenuItem quitMenuItem;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem aboutMenuItem;

    /**
     * Menu item: File->New.
     * Creates a new model in the main app.
     * Doesn't save the currently opened one!
     */
    @FXML
    private void handleNew() {
        openedFile = null;
        studyGuideApplication.setStudyPlan(new DefaultStudyPlan());
        studyGuideApplication.reinitialize();
    }

    /**
     * Menu item: File->Open.
     * Opens an existing model into the main app.
     * Doesn't save the currently opened one!
     */
    @FXML
    private void handleOpen() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON StudyPlan", ".json", ".JSON"));
        File chosen = chooser.showOpenDialog(studyGuideApplication.getPrimaryStage());
        if (chosen == null) {
            return;
        }
        openFromFile(chosen);
    }

    /**
     * Menu item: File->Save.
     * Save the opened model from the main app.
     * If there is no opened file and the model is not null, calls {@link #handleSaveAs()}.
     */
    @FXML
    private void handleSave() {
        if (openedFile == null && studyGuideApplication.getStudyPlan() != null) {
            handleSaveAs();
            return;
        }
        saveToFile(openedFile);
    }

    /**
     * Menu item: File->Save As.
     * Save the opened model from the main app into a new file.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON StudyPlan", ".json", ".JSON"));
        File chosen = chooser.showSaveDialog(studyGuideApplication.getPrimaryStage());
        if (chosen == null) {
            return;
        }
        saveToFile(chosen);
    }

    /**
     * Menu item: File->Quit.
     * Exit the main app.
     * Doesn't save the currently opened model!
     */
    @FXML
    private void handleQuit() {
        System.exit(0);
    }

    /**
     * Menu item: Help->About.
     * Shows a short modal about dialog.
     */
    @FXML
    private void handleAbout() {
        Dialog<Label> dialog = new Dialog<>();
        dialog.setContentText(
                "                               StudyGuide\n" + "    <https://github.com/oskopek/StudyGuide>\n"
                        + AbstractFXMLPane.messages.getString("menu.author") + ": Ondrej Skopek <oskopek@matfyz.cz>");
        dialog.setTitle(AbstractFXMLPane.messages.getString("root.about"));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    /**
     * Saves the currently opened model to a file.
     * If an {@link IOException} occurs, opens a modal dialog window notifying the user and prints a stack trace.
     *
     * @param file if null, does nothing
     * @see StudyGuideApplication#getStudyPlan()
     */
    private void saveToFile(File file) {
        if (file == null) {
            logger.debug("Cannot save to null file.");
            return;
        }
        try {
            writer.writeTo(studyGuideApplication.getStudyPlan(), file.getAbsolutePath());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save study plan: " + e);
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Loads a model from a file into the main app.
     * If an {@link IOException} occurs, opens a modal dialog window notifying the user and prints a stack trace.
     *
     * @param file if null, does nothing
     * @see StudyGuideApplication#setStudyPlan(com.oskopek.studyguide.model.StudyPlan)
     */
    private void openFromFile(File file) {
        if (file == null) {
            logger.debug("Cannot open from null file.");
            return;
        }
        try {
            studyGuideApplication.setStudyPlan(reader.readFrom(file.getAbsolutePath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open study plan: " + e);
            alert.showAndWait();
            e.printStackTrace();
        }
        if (studyGuideApplication.getStudyPlan() != null) {
            openedFile = file;
        }
        studyGuideApplication.reinitialize();
    }

}
