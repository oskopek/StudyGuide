package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.model.constraints.Constraints;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An event that passes a immutable copy of broken {@link Constraints} to the view.
 */
public class BrokenConstraintsEvent {

    private Constraints brokenConstraints;

    /**
     * Default constructor.
     *
     * @param brokenConstraints the broken constraints to display
     */
    public BrokenConstraintsEvent(Constraints brokenConstraints) {
        this.brokenConstraints = brokenConstraints;
    }

    /**
     * Get the broken constraints.
     *
     * @return the broken constraints
     */
    public Constraints getBrokenConstraints() {
        return brokenConstraints;
    }

    @Override
    public String toString() {
        return "BrokenConstraintsEvent[" + brokenConstraints + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BrokenConstraintsEvent)) {
            return false;
        }
        BrokenConstraintsEvent that = (BrokenConstraintsEvent) o;
        return new EqualsBuilder()
                .append(getBrokenConstraints(), that.getBrokenConstraints())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getBrokenConstraints())
                .toHashCode();
    }
}
