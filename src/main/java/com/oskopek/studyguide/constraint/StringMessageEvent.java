package com.oskopek.studyguide.constraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.inject.Inject;
import java.util.ResourceBundle;

/**
 * Event with a message contained. Used for sending useful tooltip contents to the UI controllers do display to the
 * user.
 */
public abstract class StringMessageEvent {

    @Inject
    protected transient ResourceBundle messages;

    private String message;

    /**
     * Private default constructor, needed by CDI.
     */
    private StringMessageEvent() {
        // needed for CDI
    }

    /**
     * Constructs a new message event with the given message.
     *
     * @param message the message to broadcast
     */
    public StringMessageEvent(String message) {
        this.message = message;
    }

    /**
     * Get the contained message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Format the message in a way that is more suitable for the user. Use {@link #getMessage()} as a base.
     *
     * @return the formatted and transformed message
     */
    public abstract String message();

    @Override
    public String toString() {
        return "StringMessageEvent[" + message + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StringMessageEvent)) {
            return false;
        }
        StringMessageEvent that = (StringMessageEvent) o;
        return new EqualsBuilder()
                .append(getMessage(), that.getMessage())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getMessage())
                .toHashCode();
    }
}
