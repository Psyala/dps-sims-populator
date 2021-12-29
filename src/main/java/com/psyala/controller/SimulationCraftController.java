package com.psyala.controller;

import com.psyala.model.sim.Simulation;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class SimulationCraftController {
    private final ConfigurationController configurationController;
    private final String simcInputDir = "simc_inputs";
    private String simcVersionInfo = null;
    private File simcExe = null;

    SimulationCraftController(ConfigurationController configurationController) throws IOException {
        //Ensure install dir exists
        this.configurationController = configurationController;

        //Get Simc Version
        try {
            simcExe = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\simc.exe").toAbsolutePath().toFile();
            if (simcExe.exists()) {
                simcVersionInfo = runCMD(simcExe.getAbsolutePath());
                File simcInput = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcInputDir).toFile();
                if (!simcInput.exists()) simcInput.mkdirs();
                FileUtils.cleanDirectory(simcInput);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Optional<String> getSimcVersionInfo() {
        return Optional.ofNullable(simcVersionInfo);
    }

    Optional<String> runSimulation(Simulation simulation) {
        try {
            //Create file to input to simc command
            String simcFileName = simulation.getName() + "_" + System.nanoTime() + ".simc";
            File simcFile = Paths.get(configurationController.simcInstallDir.getAbsolutePath() + "\\" + simcInputDir + "\\" + simcFileName).toFile();
            if (simcFile.exists()) simcFile.delete();
            Files.write(simcFile.toPath(), simulation.getSimulationBody().getBytes(StandardCharsets.UTF_8));

            //TODO: Run the simulation

            //TODO: Return some form of result, raw text? SimulationController should probably convert to any required objects
            return Optional.of("Success");
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    private String runCMD(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            sb.append(line);
        }

        process.waitFor();
        return sb.toString();
    }
}
