package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.MenuBarController;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * The root pane.
 * Return a VBox.
 */
public class RootLayoutPane extends AbstractFXMLPane {

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        VBox findCoursePane = (VBox) super.load(studyGuideApplication);
        MenuBarController controller = (MenuBarController) getController();
        controller.setViewElement(this);
        controller.setStudyGuideApplication(studyGuideApplication);
        return findCoursePane;
    }

    @Override
    protected String getFxmlResource() {
        return "RootLayoutPane.fxml";
    }
}
