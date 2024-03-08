package app;

import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.core.util.JacksonFeature;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Register your REST resource classes here
        packages("app.controller"); // Replace with the package containing your resource classes
        register(JacksonFeature.class);
    }
}