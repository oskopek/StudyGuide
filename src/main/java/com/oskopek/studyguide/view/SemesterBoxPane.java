package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.SemesterBoxController;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * BorderPane
 */
public class SemesterBoxPane extends AbstractFXMLPane {

    private SemesterPane parent;

    public SemesterBoxPane(SemesterPane parent) {
        this.parent = parent;
    }

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        BorderPane semesterBox = (BorderPane) super.load(studyGuideApplication);
        SemesterBoxController controller = (SemesterBoxController) getController();
        controller.setStudyGuideApplication(studyGuideApplication);
        controller.setViewElement(this);
        return semesterBox;
    }

    @Override
    protected String getFxmlResource() {
        return "SemesterBoxPane.fxml";
    }

    public SemesterPane getParent() {
        return parent;
    }
}
