package com.oskopek.studyguide.view;

import com.oskopek.studyguide.controller.SemesterBoxController;
import com.oskopek.studyguide.controller.SemesterController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * Panel for displaying courses in a single semesters. Returns a BorderPane.
 *
 * @see SemesterPane
 */
public class SemesterBoxPane extends AbstractFXMLPane {

    private SemesterPane parent;

    @FXML
    private BorderPane boxBorderPane;

    /**
     * Create a semester box from the parent {@link SemesterPane}.
     *
     * @param parent the parent containing all semester boxes
     */
    public SemesterBoxPane(SemesterPane parent) {
        this.parent = parent;
    }

    @Override
    public Node load(StudyGuideApplication studyGuideApplication) {
        BorderPane semesterBox = (BorderPane) super.load(studyGuideApplication);
        SemesterBoxController controller = (SemesterBoxController) getController();
        controller.setStudyGuideApplication(studyGuideApplication);
        controller.setViewElement(this);
        controller.initializeSemester();
        return semesterBox;
    }

    @Override
    protected String getFxmlResource() {
        return "SemesterBoxPane.fxml";
    }

    /**
     * Parent {@link SemesterPane}.
     *
     * @return the parent pane containing all semester boxes
     */
    public SemesterPane getParent() {
        return parent;
    }

    /**
     * Get the JavaFX {@link BorderPane} object that represents this semester box.
     * Needed for {@link SemesterController#onAddSemester()}.
     *
     * @return the pane representing this semester box
     */
    public BorderPane getBoxBorderPane() {
        return boxBorderPane;
    }

    /**
     * Set the JavaFX {@link BorderPane} object that represents this semester box.
     *
     * @param boxBorderPane the boxBorderPane to set
     * @see SemesterController#onAddSemester()
     */
    public void setBoxBorderPane(BorderPane boxBorderPane) {
        this.boxBorderPane = boxBorderPane;
    }
}
