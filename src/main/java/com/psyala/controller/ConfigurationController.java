package com.psyala.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationController {
    private static final String configFileName = "configuration.properties";

    final File simcInstallDir;
    final int simcThreads;

    public ConfigurationController() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(configFileName);
        if (is == null) {
            System.err.println("Failed to find ".concat(configFileName));
        }

        Properties properties = new Properties();
        properties.load(is);

        simcInstallDir = Paths.get(properties.getProperty("simc.install_location")).toAbsolutePath().toFile();
        simcThreads = Integer.parseInt(properties.getProperty("simc.threads"));
    }
}
