package com.oskopek.studyguide.view;

/**
 * List of courses and credits and isFulfilled (replicates {@link com.oskopek.studyguide.model.SemesterPlan}).
 * Returns a BorderPane.
 */
public class SemesterPane extends AbstractFXMLPane {

    @Override
    protected String getFxmlResource() {
        return "SemesterPane.fxml";
    }
}
