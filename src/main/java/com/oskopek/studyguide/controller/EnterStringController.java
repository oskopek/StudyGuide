package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.view.EnterStringDialogPaneCreator;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Controller for choosing a course out of several choices.
 */
public class EnterStringController extends AbstractController {

    private Dialog<ButtonType> dialog;

    @FXML
    private TextField textField;

    /**
     * Handles submitting the dialog in case the presses enter into the found course table.
     *
     * @param event the generated event
     */
    @FXML
    private void handleOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            applyDialog();
        }
    }

    /**
     * Closes the dialog as if the "Apply" button was clicked.
     */
    private void applyDialog() {
        dialog.resultProperty().setValue(ButtonType.APPLY);
        dialog.close();
    }

    /**
     * Get the input text field.
     *
     * @return the text field, may be null
     */
    public TextField getTextField() {
        return textField;
    }

    /**
     * Get the dialog (used for reporting double clicks in the table).
     *
     * @return the dialog
     */
    public Dialog<ButtonType> getDialog() {
        return dialog;
    }

    /**
     * Set the dialog (used for reporting double clicks in the table).
     *
     * @param dialog the dialog wrapper for {@link EnterStringDialogPaneCreator}
     */
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }

    /**
     * Get the string the user submitted.
     *
     * @return the submitted string
     */
    public String getSubmittedString() {
        return textField.getText();
    }
}
