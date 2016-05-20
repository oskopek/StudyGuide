package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.model.DefaultStudyPlan;
import com.oskopek.studyguide.model.StudyPlan;
import com.oskopek.studyguide.persistence.DataWriter;
import com.oskopek.studyguide.persistence.JsonDataReaderWriter;
import com.oskopek.studyguide.persistence.MFFHtmlScraper;
import com.oskopek.studyguide.persistence.MFFWebScraperUtil;
import com.oskopek.studyguide.view.AlertCreator;
import com.oskopek.studyguide.view.EnterStringDialogPaneCreator;
import com.oskopek.studyguide.view.ProgressCreator;
import com.oskopek.studyguide.view.StudyGuideApplication;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Handles all action in the menu bar of the main app.
 */
@Singleton
public class RootLayoutController extends AbstractController {

    private final DataWriter writer = new JsonDataReaderWriter();

    private File openedFile;

    @Inject
    private SemesterController semesterController;

    @Inject
    private FindCoursesController findCoursesController;

    @Inject
    private EnterStringDialogPaneCreator enterStringDialogPaneCreator;

    @Inject
    private transient Logger logger;

    /**
     * Menu item: File->New.
     * Creates a new model in the main app.
     * Doesn't save the currently opened one!
     */
    @FXML
    private void handleNew() {
        openedFile = null;
        studyGuideApplication.setStudyPlan(new DefaultStudyPlan());
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
     * Menu item: File->Open From....
     * Scrape a study plan off the network resource and load it into the main app.
     * Doesn't save the currently opened one!
     */
    @FXML
    private void handleOpenFrom() {
        EnterStringController enterStringController = enterStringDialogPaneCreator
                .create(messages.getString("root.enterUrl"));
        Optional<ButtonType> result = enterStringController.getDialog().showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            String submittedURL = enterStringController.getSubmittedString();
            MFFHtmlScraper scraper = new MFFHtmlScraper(MFFWebScraperUtil.sisWebUrl);
            Stage progressDialog = ProgressCreator.showProgress(scraper, messages.getString("progress.pleaseWait"));
            Task<StudyPlan> studyPlanTask = new Task<StudyPlan>() {
                @Override
                protected StudyPlan call() throws Exception {
                    return scraper.scrapeStudyPlan(submittedURL);
                }
            };
            studyPlanTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
                progressDialog.close();
                AlertCreator.showAlert(Alert.AlertType.ERROR,
                        messages.getString("root.openFromFailed") + ":\n\n" + newValue.getLocalizedMessage());
                throw new IllegalStateException("Open from failed.", newValue);
            });
            studyPlanTask.setOnSucceeded(event -> {
                progressDialog.close();
                studyGuideApplication.setStudyPlan(studyPlanTask.getValue());
                openedFile = null;
            });
            studyPlanTask.setOnFailed(event -> {
                progressDialog.close();
                AlertCreator.showAlert(Alert.AlertType.ERROR, messages.getString("root.openFromFailed"));
            });

            new Thread(studyPlanTask).start();
        }
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
                        + messages.getString("menu.author") + ": Ondrej Skopek <oskopek@matfyz.cz>");
        dialog.setTitle(messages.getString("root.about"));
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
            AlertCreator.showAlert(Alert.AlertType.ERROR, "Failed to save study plan: " + e);
            e.printStackTrace();
        }
        openedFile = file;
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
            JsonDataReaderWriter reader = new JsonDataReaderWriter(messages, eventBus);
            StudyPlan studyPlan = reader.readFrom(file.getAbsolutePath());
            studyGuideApplication.setStudyPlan(studyPlan);
            studyPlan.getConstraints().recheckAll();
        } catch (IOException e) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, "Failed to open study plan: " + e);
            e.printStackTrace();
        }
        if (studyGuideApplication.getStudyPlan() != null) {
            openedFile = file;
        }
    }
}
