package com.oskopek.studyguide.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

/**
 * Controller for choosing a course out of several choices.
 */
public class ChooseURLController extends AbstractController {

    private Dialog<ButtonType> dialog;

    @FXML
    private TextField urlField;

    /**
     * Closes the dialog as if the "Apply" button was clicked.
     */
    private void applyDialog() {
        dialog.resultProperty().setValue(ButtonType.APPLY);
        dialog.close();
    }

    /**
     * Set the dialog (used for reporting double clicks in the table).
     * @param dialog the dialog wrapper for {@link com.oskopek.studyguide.view.ChooseURLDialogPaneCreator}
     */
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    /**
     * Get the dialog (used for reporting double clicks in the table).
     * @return the dialog
     */
    public Dialog<ButtonType> getDialog() {
        return dialog;
    }

    public String getSubmittedURL() {
        return urlField.getText();
    }
}
