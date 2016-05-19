package com.oskopek.studyguide.model.courses;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * An abstraction of the ECTS credit value for a {@link Course}.
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
public final class Credits extends ObservableValueBase<Credits>
        implements Comparable<Credits>, ObservableValue<Credits> {

    private final IntegerProperty creditValue;

    /**
     * Empty JSON constructor.
     */
    private Credits() {
        this(0);
    }

    /**
     * Doesn't check the creditValue's value.
     * Please use {@link Credits#valueOf(int)}.
     *
     * @param creditValue any integer
     */
    private Credits(int creditValue) {
        this.creditValue = new SimpleIntegerProperty(creditValue);
        this.creditValue.addListener((x, y, z) -> fireValueChangedEvent());
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
        return creditValue.get();
    }

    /**
     * Sets the credit value into {@link #creditValueProperty()}.
     *
     * @param creditValue greater than 0
     */
    public void setCreditValue(int creditValue) {
        if (creditValue < 0) {
            throw new IllegalArgumentException("Credit value " + creditValue + " is less than 0.");
        }
        this.creditValue.set(creditValue);
    }

    /**
     * The JavaFX property for {@link #getCreditValue()}.
     *
     * @return the property of {@link #getCreditValue()}
     */
    public IntegerProperty creditValueProperty() {
        return creditValue;
    }

    @Override
    @JsonIgnore
    public Credits getValue() {
        return Credits.valueOf(getCreditValue());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCreditValue()).toHashCode();
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
        return new EqualsBuilder().append(getCreditValue(), credits.getCreditValue()).isEquals();
    }

    @Override
    public String toString() {
        return "Credits[" + getCreditValue() + ']';
    }

    @Override
    public int compareTo(Credits o) {
        return new CompareToBuilder().append(getCreditValue(), o.getCreditValue()).toComparison();
    }
}
