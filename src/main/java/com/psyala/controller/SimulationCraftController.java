package com.psyala.controller;

import com.psyala.model.sim.Simulation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

public class SimulationCraftController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationCraftController.class);
    private final ConfigurationController configurationController;
    private final String simcInputDir = "simc_input";
    private final String simcOutputDir = "simc_output";
    private String simcVersionInfo = null;
    private File simcExe = null;

    SimulationCraftController(ConfigurationController configurationController) throws IOException {
        //Ensure install dir exists
        this.configurationController = configurationController;

        //Get Simc Version
        try {
            simcExe = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\simc.exe").toAbsolutePath().toFile();
            if (simcExe.exists()) {
                simcVersionInfo = exec("\"" + simcExe.getAbsolutePath() + "\"").trim();

                File simcInput = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcInputDir).toFile();
                File simcOutput = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcOutputDir).toFile();
                for (File file : Arrays.asList(simcInput, simcOutput)) {
                    if (!file.exists()) file.mkdirs();
                    FileUtils.cleanDirectory(file);
                }
            }

        } catch (InterruptedException ex) {
            LOGGER.error("Error initialising SimulationCraft", ex);
        }
    }

    public Optional<String> getSimcVersionInfo() {
        return Optional.ofNullable(simcVersionInfo);
    }

    String runSimulation(Simulation simulation) {
        try {
            //Create file to input to simc command
            String simcFileName = simulation.getName() + "_" + System.nanoTime() + ".simc";
            File simcInputFile = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcInputDir + "\\" + simcFileName).toFile();
            File simcOutputFile = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcOutputDir + "\\" + simcFileName).toFile();
            Arrays.asList(simcInputFile, simcOutputFile).forEach(file -> file.delete());
            Files.write(simcInputFile.toPath(), simulation.getSimulationBody().getBytes(StandardCharsets.UTF_8));

            //Create Command to Run
            String command = "\"".concat(simcExe.getAbsolutePath()).concat("\"")
                    .concat(" \"").concat(simcInputFile.getAbsolutePath()).concat("\"");

            //Run the simulation
            String simcOutputString = exec(command);
            Files.write(simcOutputFile.toPath(), simcOutputString.getBytes(StandardCharsets.UTF_8));
            return simcOutputString;
        } catch (Exception ex) {
            LOGGER.error("Error running simulation: " + simulation.getName(), ex);
            return "";
        }
    }

    private String exec(String command) throws IOException, InterruptedException {
        LOGGER.debug("Starting: " + command);
        Process p = Runtime.getRuntime().exec(command);

        String standardText = "";
        String errorText = "";
        boolean processComplete = false;
        while (!processComplete) {
            standardText = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
            errorText = IOUtils.toString(p.getErrorStream(), StandardCharsets.UTF_8);

            if (errorText.contains("Nothing to sim!") || standardText.contains("text report took")) {
                LOGGER.debug("Command Completed: " + command);
                processComplete = true;
            } else {
                LOGGER.debug("Command Not Completed: " + command);
                Thread.sleep(1000);
            }
        }

        LOGGER.debug(
                "\r\n\r\n===============================\r\n"
                        + "Command: " + command + "\r\n"
                        + "Input Stream:\r\n"
                        + standardText + "\r\n"
                        + "Error Stream:\r\n"
                        + errorText + "\r\n"
                        + "===============================\r\n\r\n"
        );

        p.destroy();
        return standardText;
    }

}
