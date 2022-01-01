package com.psyala.controller;

import com.psyala.model.Run;
import com.psyala.model.sim.Simulation;
import com.psyala.util.JsonParser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class SimulationCraftController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationCraftController.class);
    private final ConfigurationController configurationController;
    private final DebugOutputController debugOutputController;
    private final String simcInputDir = "simc_input";
    private final String simcOutputDir = "simc_output";
    private final String runOutputDir = "run_output";
    private String simcVersionInfo = null;
    private File simcExe = null;

    public SimulationCraftController(ConfigurationController configurationController, DebugOutputController debugOutputController) {
        //Ensure install dir exists
        this.configurationController = configurationController;
        this.debugOutputController = debugOutputController;

        //Get Simc Version
        try {
            //TODO: Get latest version of simc if not installed

            simcExe = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\simc.exe").toAbsolutePath().toFile();
            if (simcExe.exists()) {
                simcVersionInfo = exec("\"" + simcExe.getAbsolutePath() + "\"").trim();

                File simcInput = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcInputDir).toFile();
                File simcOutput = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcOutputDir).toFile();
                File runOutput = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + runOutputDir).toFile();
                for (File file : Arrays.asList(simcInput, simcOutput, runOutput)) {
                    if (!file.exists()) file.mkdirs();
                    if (file != runOutput) FileUtils.cleanDirectory(file);
                }
            }

        } catch (InterruptedException | IOException ex) {
            LOGGER.error("Error initialising SimulationCraft", ex);
        }
    }

    public Optional<String> getSimcVersionInfo() {
        return Optional.ofNullable(simcVersionInfo);
    }

    public void storeRun(Date runTime, Run run) throws IOException {
        String fileName = "Run_".concat(String.valueOf(runTime.toInstant().getNano())).concat(".json");
        File runOutputFile = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + runOutputDir + "\\" + fileName).toFile();
        Files.write(runOutputFile.toPath(), JsonParser.toJson(run).getBytes(StandardCharsets.UTF_8));
    }

    String runSimulation(Simulation simulation) {
        try {
            //Create file to input to simc command
            String simcFileName = simulation.getName().concat("_").concat(String.valueOf(System.nanoTime()));
            File simcInputFile = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcInputDir + "\\" + simcFileName + ".simc").toFile();
            File simcJsonOutputFile = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcOutputDir + "\\" + simcFileName + ".json").toFile();
            Arrays.asList(simcInputFile, simcJsonOutputFile).forEach(file -> file.delete());

            //Write input file
            String fileContents = simulation.getSimulationBody()
                    .concat("\r\n\r\njson2=\"").concat(simcJsonOutputFile.getAbsolutePath()).concat("\"");
            Files.write(simcInputFile.toPath(), fileContents.getBytes(StandardCharsets.UTF_8));

            //Create Command to Run
            String command = "\"".concat(simcExe.getAbsolutePath()).concat("\"")
                    .concat(" \"").concat(simcInputFile.getAbsolutePath()).concat("\"");

            //Run the simulation
            File simcOutputFile = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcOutputDir + "\\" + simcFileName + ".txt").toFile();
            File simcOutputErrorFile = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcOutputDir + "\\" + simcFileName + "_error.txt").toFile();
            exec(command, debugOutputController.registerSimcThread(simulation), simcOutputFile, simcOutputErrorFile);

            //Get output JSON from simcJsonOutputFile
            return String.join("\r\n", Files.readAllLines(simcJsonOutputFile.toPath(), StandardCharsets.UTF_8));
        } catch (Exception ex) {
            LOGGER.error("Error running simulation: " + simulation.getName(), ex);
            return "";
        }
    }

    private String exec(String command) throws IOException, InterruptedException {
        File outputFile = new File("temp.txt");
        File outputErrorFile = new File("temp_error.txt");
        String output = exec(command, null, outputFile, outputErrorFile);
        outputFile.delete();
        outputErrorFile.delete();
        return output;
    }

    private String exec(String command, JTextArea jTextArea, File commandOutputFile, File commandOutputErrorFile) throws IOException, InterruptedException {
        LOGGER.debug("Starting: " + command);

        FileUtils.write(commandOutputFile, "", StandardCharsets.UTF_8);
        FileUtils.write(commandOutputErrorFile, "", StandardCharsets.UTF_8);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectOutput(commandOutputFile);
        pb.redirectError(commandOutputErrorFile);
        pb.start();

        String standardText = "";
        String errorText = "";
        boolean processComplete = false;
        while (!processComplete) {
            standardText = String.join("\r\n", FileUtils.readLines(commandOutputFile, StandardCharsets.UTF_8));
            errorText = String.join("\r\n", FileUtils.readLines(commandOutputErrorFile, StandardCharsets.UTF_8));

            //If a text area is provided , update with text
            if (jTextArea != null) {
                String finalStandardText = standardText;
                SwingUtilities.invokeLater(() -> jTextArea.setText(finalStandardText));
            }

            //Decide if command has finished
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
        
        return standardText;
    }


}
