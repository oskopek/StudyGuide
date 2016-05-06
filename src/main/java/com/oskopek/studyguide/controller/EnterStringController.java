package com.oskopek.studyguide.controller;

import com.oskopek.studyguide.view.EnterStringDialogPaneCreator;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Controller for choosing a course out of several choices.
 */
public class EnterStringController extends AbstractController {

    private Dialog<ButtonType> dialog;

    @FXML
    private TextField urlField;

    /**
     * Handles submitting the dialog in case the user double clicks into the found course table.
     *
     * @param event the generated event
     */
    @FXML
    private void handleOnMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            applyDialog();
        }
    }

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
     * Set the dialog (used for reporting double clicks in the table).
     *
     * @param dialog the dialog wrapper for {@link EnterStringDialogPaneCreator}
     */
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
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
     * Get the string the user submitted.
     *
     * @return the submitted string
     */
    public String getSubmittedString() {
        return urlField.getText();
    }
}
