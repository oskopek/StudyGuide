package com.oskopek.studyguide.weld;

import javafx.fxml.FXMLLoader;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ResourceBundle;

/**
 * Simple {@link FXMLLoader} producer, including loading of the {@code messages} resource bundle.
 */
public class FXMLLoaderProducer {

    @Inject
    private Instance<Object> instance;

    @Inject
    private ResourceBundle messages;

    /**
     * Produces an {@link FXMLLoader} including the {@code messages} resource bundle. Useful for in all places
     * where an {@code *.fxml} component file is being loaded into a JavaFX container.
     *
     * @return an initialized FXML loader
     */
    @Produces
    @Named("fxmlloader")
    public FXMLLoader createLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(messages);
        loader.setControllerFactory(param -> instance.select(param).get());
        return loader;
    }
}
