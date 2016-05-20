package com.oskopek.studyguide.constraint.event;

import com.oskopek.studyguide.constraint.Constraint;

import java.util.ResourceBundle;

/**
 * The event used for reporting broken global constraints.
 */
public class BrokenGlobalConstraintEvent extends StringMessageEvent {

    /**
     * Default constructor.
     *
     * @param messages the resource bundle used for resolving messages to their localized versions
     * @param message the message to use as a reason why the constraint is broken
     * @param broken the constraint that was broken and generated this event
     */
    public BrokenGlobalConstraintEvent(ResourceBundle messages, String message, Constraint broken) {
        super(messages, message, broken);
    }

    @Override
    public String message() {
        return messages.getString("constraint.globalInvalid") + " " + getMessage();
    }
}
