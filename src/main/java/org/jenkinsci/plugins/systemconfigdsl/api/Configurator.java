package org.jenkinsci.plugins.systemconfigdsl.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import hudson.model.Run;
import org.jenkinsci.plugins.systemconfigdsl.Validator;
import org.jenkinsci.plugins.systemconfigdsl.error.ValidationException;

import java.io.IOException;

public abstract class Configurator {

    public abstract String getConfigFileSectionName();

    public abstract void configure(final String config, final boolean dryRun);

    public abstract boolean isConfigurationValid(final String config);

    protected boolean isSchemaValid(final String config, final String schema) {
        boolean status = true;
        final Validator validator = new Validator("schema/" + schema + ".json");
        try {
            validator.validate(config);
        } catch (ValidationException e) {
            status = false;
        }
        return status;
    }

    protected ConfigurationDescription parseConfiguration(final String config, final Class<? extends ConfigurationDescription> configurationDescriptionClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(config, configurationDescriptionClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}