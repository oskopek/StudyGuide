package com.oskopek.studyguide.weld;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.ResourceBundle;

/**
 * A simple {@link ResourceBundle} producer for the {@code messages} properties resource bundle.
 */
public class MessagesProducer {

    /**
     * Create a {@link ResourceBundle} {@code messages}, used for localization in the UI.
     * @return an initialized resource bundle used for localization
     */
    @Produces
    @Singleton
    public ResourceBundle createMessagesResourceBundle() {
        return ResourceBundle.getBundle("com.oskopek.studyguide.view.messages");
    }

}
