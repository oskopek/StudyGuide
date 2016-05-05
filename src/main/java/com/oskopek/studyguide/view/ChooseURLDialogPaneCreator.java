package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.ChooseCourseController;
import com.oskopek.studyguide.controller.ChooseURLController;
import com.oskopek.studyguide.model.courses.Course;
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
import java.util.List;

/**
 * Dialog for entering a URL. Creates a DialogPane encapsulated in it's controller.
 *
 * @see ChooseCourseController
 */
@Singleton
public class ChooseURLDialogPaneCreator {

    @Inject
    @Named("fxmlloader")
    private Instance<FXMLLoader> fxmlLoader;

    /**
     * Create the dialog for entering a URL.
     *
     * @return the controller of the dialog window, enabling to display the dialog and read the selected result
     */
    public ChooseURLController create() {
        FXMLLoader fxmlLoader = this.fxmlLoader.get();
        DialogPane dialogPane = null;
        try (InputStream is = getClass().getResourceAsStream("ChooseURLDialogPaneCreator.fxml")) {
            dialogPane = fxmlLoader.load(is);
        } catch (IOException e) {
            AlertCreator.handleLoadLayoutError(fxmlLoader.getResources(), e);
        }
        ChooseURLController chooseURLController = fxmlLoader.getController();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        chooseURLController.setDialog(dialog);
        return chooseURLController;
    }
}
