package com.oskopek.studyguide.persistence;

import javafx.beans.property.DoubleProperty;

/**
 * Enables an object to be observable for internal progress.
 *
 * @see MFFHtmlScraper
 * @see SISHtmlScraper
 */
public interface ProgressObservable {

    /**
     * The double property that is bound to a UI element showing progress.
     * Expects values either equal to {@code -1d} or in the range {@code [0, 1]}.
     *
     * @return the double property
     */
    DoubleProperty progressProperty();

}
