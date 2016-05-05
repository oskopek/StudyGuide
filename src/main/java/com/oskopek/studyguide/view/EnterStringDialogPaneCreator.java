package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.EnterStringController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;

/**
 * Dialog for entering a String. Creates a DialogPane encapsulated in it's controller.
 */
@Singleton
public class EnterStringDialogPaneCreator {

    @Inject
    @Named("fxmlloader")
    private Instance<FXMLLoader> fxmlLoader;

    /**
     * Create the dialog for entering a String.
     *
     * @param prompt the string message to prompt the user with
     * @return the controller of the dialog window, enabling to display the dialog and read the selected result
     */
    public EnterStringController create(String prompt) {
        FXMLLoader fxmlLoader = this.fxmlLoader.get();
        DialogPane dialogPane = null;
        try (InputStream is = getClass().getResourceAsStream("EnterStringDialogPane.fxml")) {
            dialogPane = fxmlLoader.load(is);
        } catch (IOException e) {
            AlertCreator.handleLoadLayoutError(fxmlLoader.getResources(), e);
        }
        dialogPane.setHeaderText(prompt);
        EnterStringController enterStringController = fxmlLoader.getController();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        enterStringController.setDialog(dialog);
        return enterStringController;
    }
}
