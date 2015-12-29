package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.persistence.DataReader;
import com.oskopek.studyguide.persistence.DataWriter;
import com.oskopek.studyguide.persistence.JsonDataReaderWriter;
import com.oskopek.studyguide.view.AbstractFXMLPane;
import com.oskopek.studyguide.view.RootLayoutPane;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

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

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleNew() {
        handleSave(); // auto-save
        openedFile = null;
        studyGuideApplication.setStudyPlan(new DefaultStudyPlan());
    }

    @FXML
    private void handleOpen() {
        handleSave(); // auto-save
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON StudyPlan", ".json", ".JSON"));
        File chosen = chooser.showOpenDialog(studyGuideApplication.getPrimaryStage());
        if (chosen == null) {
            return;
        }
        openFromFile(chosen);
    }


    @FXML
    private void handleClose() {
        handleSave(); // auto-save
        openedFile = null;
        studyGuideApplication.setStudyPlan(null);
    }

    @FXML
    private void handleSave() {
        if (openedFile == null && studyGuideApplication.getStudyPlan() != null) {
            handleSaveAs();
            return;
        }
        saveToFile(openedFile);
    }

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

    @FXML
    private void handleQuit() {
        System.exit(0);
    }

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

    private void saveToFile(File file) {
        if (file == null) {
            logger.debug("Cannot save to null file.");
            return;
        }
        try {
            writer.writeTo(studyGuideApplication.getStudyPlan(), file.getAbsolutePath().toString());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save study plan: " + e);
            alert.showAndWait();
        }
    }

    private void openFromFile(File file) {
        if (file == null) {
            logger.debug("Cannot open from null file.");
            return;
        }
        try {
            studyGuideApplication.setStudyPlan(reader.readFrom(file.getAbsolutePath().toString()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open study plan: " + e);
            alert.showAndWait();
        }
        if (studyGuideApplication.getStudyPlan() != null) {
            openedFile = file;
        }
    }

}
