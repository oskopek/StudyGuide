package com.oskopek.studyguide.constraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The event used for removing UI element warnings on broken constraints.
 */
public class BrokenResetEvent {

    private Constraint originallyBroken;

    /**
     * Default constructor.
     *
     * @param originallyBroken the constraint that is not broken anymore
     */
    public BrokenResetEvent(Constraint originallyBroken) {
        this.originallyBroken = originallyBroken;
    }

    /**
     * Get the constraint that is not broken anymore.
     *
     * @return the constraint
     */
    public Constraint getOriginallyBroken() {
        return originallyBroken;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getOriginallyBroken()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BrokenResetEvent)) {
            return false;
        }
        BrokenResetEvent that = (BrokenResetEvent) o;
        return new EqualsBuilder().append(getOriginallyBroken(), that.getOriginallyBroken()).isEquals();
    }

    @Override
    public String toString() {
        return "BrokenResetEvent[" + originallyBroken + "]";
    }
}
