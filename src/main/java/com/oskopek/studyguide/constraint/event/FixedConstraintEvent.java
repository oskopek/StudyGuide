package com.oskopek.studyguide.constraint.event;

import com.oskopek.studyguide.constraint.Constraint;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The event used for removing UI element warnings on broken constraints.
 */
public class FixedConstraintEvent {

    private final Constraint originallyBroken;

    /**
     * Default constructor.
     *
     * @param originallyBroken the constraint that is not broken anymore
     */
    public FixedConstraintEvent(Constraint originallyBroken) {
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
        if (!(o instanceof FixedConstraintEvent)) {
            return false;
        }
        FixedConstraintEvent that = (FixedConstraintEvent) o;
        return new EqualsBuilder().append(getOriginallyBroken(), that.getOriginallyBroken()).isEquals();
    }

    @Override
    public String toString() {
        return "FixedConstraintEvent[" + originallyBroken + "]";
    }
}
