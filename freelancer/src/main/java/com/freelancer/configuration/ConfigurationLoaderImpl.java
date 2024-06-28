package com.freelancer.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationLoaderImpl implements ConfigurationLoader {

    private String configFilePath;

    public ConfigurationLoaderImpl(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    @Override
    public void load(PropertiesClass propertiesClass) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
            propertiesClass.setProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
