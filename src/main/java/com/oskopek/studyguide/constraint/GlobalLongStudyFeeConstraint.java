package com.oskopek.studyguide.constraint;

import com.oskopek.studyguide.constraint.event.StringMessageEvent;
import com.oskopek.studyguide.model.Semester;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Checks if the last non-empty semester is beyond the allowed semester count.
 * If it is, for every semester above the allowed count, you pay a fine in the given currency.
 */
public class GlobalLongStudyFeeConstraint extends GlobalConstraint {

    private static final DecimalFormat currencyFormat = new DecimalFormat("##0.00");
    private final String message = "constraint.globalLongStudyFeeInvalid";
    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    private BigDecimal feePerSemester;
    private Currency currency;
    private int fromSemester; // counted from 1

    /**
     * Private default constructor, needed by CDI.
     */
    protected GlobalLongStudyFeeConstraint() {
        // needed for CDI
    }

    /**
     * Converts the value and currency into a string in the form {@code ##00.00 CURRENCY_SYMBOL}.
     *
     * @param value the value to format
     * @param currency the currency whose symbol to append
     * @return the formatted currency value
     */
    private static String toCurrencyFormat(BigDecimal value, Currency currency) {
        return currencyFormat.format(value.doubleValue()) + " " + currency.getSymbol();
    }

    @Override
    public void validate() {
        logger.debug("Validating fees for study plan from semester {} ({} {} per semester)", fromSemester,
                feePerSemester.toPlainString(), currency.getSymbol());
        if (semesterPlan.getSemesterList().size() < fromSemester) {
            fireFixedEvent(this);
            return;
        }

        int lastNonEmptySemester = 0; // counted from 1
        List<Semester> semesterList = new ArrayList<>(semesterPlan.getSemesterList());
        for (int i = semesterList.size() - 1; i >= 0; i--) {
            if (semesterList.get(i).getCourseEnrollmentList().size() > 0) {
                lastNonEmptySemester = i + 1;
                break;
            }
        }
        int semestersOverLimit = Math.max(0, lastNonEmptySemester - fromSemester + 1);
        if (semestersOverLimit > 0) {
            logger.debug("Study plan over limit, calculated fee: {} * {}", semestersOverLimit,
                    feePerSemester.toPlainString());
            fireBrokenEvent(
                    generateMessage(semestersOverLimit, feePerSemester.multiply(BigDecimal.valueOf(semestersOverLimit)),
                            currency));
        } else {
            fireFixedEvent(this);
        }
    }

    /**
     * Generates a message from the given parameters (localized). Used for populating the message of
     * {@link StringMessageEvent}s (usually upon breaking a constraint).
     *
     * @param semestersOverLimit the number of semester over the max allowed limit
     * @param calculatedFee the total fee for all semesters over the limit
     * @param currency the currency in which the fee is calculated
     * @return the String to use as a message, localized
     */
    private String generateMessage(int semestersOverLimit, BigDecimal calculatedFee, Currency currency) {
        return String
                .format(messages.getString(message), semestersOverLimit, toCurrencyFormat(calculatedFee, currency));
    }

    /**
     * Get the fee paid for every semester above the limit.
     *
     * @return the fee, non-negative
     */
    public BigDecimal getFeePerSemester() {
        return feePerSemester;
    }

    /**
     * Set the fee paid for every semester above the limit.
     *
     * @param feePerSemester non-negative
     * @throws IllegalArgumentException if the fee is negative
     */
    public void setFeePerSemester(BigDecimal feePerSemester) throws IllegalArgumentException {
        if (feePerSemester.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fee cannot be less than 0: " + feePerSemester);
        }
        this.feePerSemester = feePerSemester;
    }

    /**
     * Get the currency in which the fee is paid.
     *
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Set the currency in which the fee is paid.
     *
     * @param currency the currency
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Get the semester (counted from 1) from which we pay a fine every semester if there is a non-empty semester
     * after (including) this one.
     *
     * @return the semester
     */
    public int getFromSemester() {
        return fromSemester;
    }

    /**
     * Set the semester (counted from 1) from which we pay a fine every semester if there is a non-empty semester
     * after (including) this one.
     *
     * @param fromSemester the semester
     */
    public void setFromSemester(int fromSemester) {
        this.fromSemester = fromSemester;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getFeePerSemester()).append(getCurrency()).append(getFromSemester())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalLongStudyFeeConstraint)) {
            return false;
        }
        GlobalLongStudyFeeConstraint that = (GlobalLongStudyFeeConstraint) o;
        return new EqualsBuilder().append(getFromSemester(), that.getFromSemester())
                .append(getFeePerSemester(), that.getFeePerSemester()).append(getCurrency(), that.getCurrency())
                .isEquals();
    }
}
