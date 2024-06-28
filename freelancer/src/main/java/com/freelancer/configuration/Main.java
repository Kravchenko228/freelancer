package com.freelancer.configuration;

public class Main {
    public static void main(String[] args) {
        PropertiesClass propertiesClass = new PropertiesClass();
        ConfigurationLoader configLoader = new ConfigurationLoaderImpl("/workspaces/freelancer/freelancer/src/main/java/resources/config.properties");
        configLoader.load(propertiesClass);

        propertiesClass.getProperties().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
    }
}
