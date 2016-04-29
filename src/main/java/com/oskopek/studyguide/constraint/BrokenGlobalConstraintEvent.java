package com.oskopek.studyguide.constraint;

public class BrokenGlobalConstraintEvent extends StringMessageEvent {

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
