package com.oskopek.studyguide.model.courses;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An abstraction of the ECTS credit value for a {@link Course}.
 */
public final class Credits {

    private int creditValue;

    /**
     * Doesn't check the creditValue's value.
     * Please use {@link Credits#valueOf(int)}.
     *
     * @param creditValue any integer
     */
    private Credits(int creditValue) {
        this.creditValue = creditValue;
    }

    /**
     * Create a new integer ECTS credit instance.
     *
     * @param creditValue non-negative
     * @return a Credits instance with a non-negative {@code creditValue}.
     * @throws IllegalArgumentException if {@code creditValue < 0}
     */
    public static Credits valueOf(int creditValue) throws IllegalArgumentException {
        if (creditValue < 0) {
            throw new IllegalArgumentException("Credit value cannot be smaller than 0: " + creditValue);
        }
        return new Credits(creditValue);
    }

    /**
     * Non-negative integer representing the ECTS credit value of a {@link Course}.
     *
     * @return non-negative
     */
    public int getCreditValue() {
        return creditValue;
    }

    @Override
    public String toString() {
        return "Credits[" + creditValue + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Credits)) {
            return false;
        }
        Credits credits = (Credits) o;
        return new EqualsBuilder().append(creditValue, credits.creditValue).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(creditValue).toHashCode();
    }
}
