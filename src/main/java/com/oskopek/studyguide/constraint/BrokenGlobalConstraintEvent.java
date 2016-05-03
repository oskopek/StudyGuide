package com.oskopek.studyguide.constraint;

/**
 * The event used for reporting broken global constraints.
 */
public class BrokenGlobalConstraintEvent extends StringMessageEvent {

    /**
     * Default constructor.
     *
     * @param message the message to use as a reason why the constraint is broken
     */
    public BrokenGlobalConstraintEvent(String message) {
        super(message);
    }

    @Override
    public String message() {
        return messages.getString("constraint.globalInvalid") + getMessage();
    }

    @Override
    public String toString() {
        return "BrokenGlobalConstraintEvent[" + getMessage() + "]";
    }
}
