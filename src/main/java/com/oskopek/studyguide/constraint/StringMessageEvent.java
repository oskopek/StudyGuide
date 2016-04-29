package com.oskopek.studyguide.constraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.inject.Inject;
import java.util.ResourceBundle;

public abstract class StringMessageEvent {

    @Inject
    protected ResourceBundle messages;

    private String message;

    private StringMessageEvent() {
        // needed for CDI
    }

    public StringMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public abstract String message();

    @Override
    public String toString() {
        return "StringMessageEvent[" + message + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof StringMessageEvent)) return false;

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
