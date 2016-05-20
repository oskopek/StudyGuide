package com.oskopek.studyguide.controller;

import com.google.common.eventbus.Subscribe;
import com.oskopek.studyguide.constraint.Constraint;
import com.oskopek.studyguide.constraint.event.BrokenCourseGroupConstraintEvent;
import com.oskopek.studyguide.constraint.event.BrokenGlobalConstraintEvent;
import com.oskopek.studyguide.constraint.event.FixedConstraintEvent;
import com.oskopek.studyguide.constraint.event.StringMessageEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller for displaying broken {@link com.oskopek.studyguide.constraint.CourseGroupConstraint}s and {@link
 * com.oskopek.studyguide.constraint.GlobalConstraint}s.
 */
public class ConstraintsController extends AbstractController {

    @FXML
    private ListView<StringMessageEvent> listView;

    @Inject
    private transient Logger logger;

    private ObservableList<StringMessageEvent> brokenConstraintEventList;

    private Set<Constraint> constraintSet;

    /**
     * Initializes the {@link #listView} data bindings.
     */
    @FXML
    private void initialize() {
        eventBus.register(this);
        brokenConstraintEventList = FXCollections.observableArrayList();
        listView.setItems(brokenConstraintEventList);
        constraintSet = new HashSet<>();
    }

    /**
     * Internal handler that does all the checking and updating the view when a broken constraint event arrives.
     *
     * @param event the broken constraint event
     */
    private void onBrokenConstraintInternal(StringMessageEvent event) {
        if (!constraintSet.contains(event.getBrokenConstraint())) {
            constraintSet.add(event.getBrokenConstraint());
        } else {
            removeAllBrokenEventsFromList(event.getBrokenConstraint());
        }
        brokenConstraintEventList.add(event); // overwrite, use the newer one always
    }

    /**
     * An {@link com.google.common.eventbus.EventBus} subscriber,
     * listening for {@link BrokenCourseGroupConstraintEvent}s.
     *
     * @param event the posted broken constraint event
     */
    @Subscribe
    public void onBrokenConstraint(BrokenCourseGroupConstraintEvent event) {
        logger.debug("CourseGroupConstraint {} broken.", event.getBrokenConstraint());
        onBrokenConstraintInternal(event);
    }

    /**
     * An {@link com.google.common.eventbus.EventBus} subscriber,
     * listening for {@link BrokenGlobalConstraintEvent}s.
     *
     * @param event the posted broken constraint event
     */
    @Subscribe
    public void onBrokenConstraint(BrokenGlobalConstraintEvent event) {
        logger.debug("GlobalConstraint {} broken.", event.getBrokenConstraint());
        onBrokenConstraintInternal(event);
    }

    /**
     * Internal util method for removing all broken constraint events from the {@link #brokenConstraintEventList}
     * which have a given constraint as their cause.
     *
     * @param constraint the originally broken constraint
     */
    private void removeAllBrokenEventsFromList(Constraint constraint) {
        brokenConstraintEventList
                .removeIf(stringMessageEvent -> stringMessageEvent.getBrokenConstraint().equals(constraint));
    }

    /**
     * An {@link com.google.common.eventbus.EventBus} subscriber,
     * listening for {@link FixedConstraintEvent}s.
     *
     * @param event the observed event
     */
    @Subscribe
    public void onFixedConstraint(FixedConstraintEvent event) {
        logger.debug("Constraint {} fixed.", event.getOriginallyBroken());
        if (constraintSet.contains(event.getOriginallyBroken())) {
            constraintSet.remove(event.getOriginallyBroken());
            removeAllBrokenEventsFromList(event.getOriginallyBroken());
        }
    }

}
