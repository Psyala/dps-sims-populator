package com.psyala.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.psyala.model.ConcurrentTask;
import com.psyala.model.SimcOutputJSON;
import com.psyala.model.sim.ProfileResult;
import com.psyala.model.sim.Simulation;
import com.psyala.model.sim.SimulationResult;
import com.psyala.util.JsonParser;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationController.class);
    private final ExecutorService simulationExecutor;
    private final SimulationCraftController simulationCraftController;

    public SimulationController(ConfigurationController configurationController, SimulationCraftController simulationCraftController) {
        simulationExecutor = Executors.newFixedThreadPool(configurationController.simcThreads, r -> new Thread(r, "Simulation Thread"));
        this.simulationCraftController = simulationCraftController;
    }

    /**
     * Shuts down the ExecutorService to allow for graceful system shutdown.
     */
    public void shutDown() {
        simulationExecutor.shutdownNow();
    }

    public SimulationCraftController getSimulationCraftController() {
        return simulationCraftController;
    }

    public Pair<String, ConcurrentTask> performSim(Simulation simulation) {
        ConcurrentTask c = new ConcurrentTask(() -> convert(simulation, simulationCraftController.runSimulation(simulation)));
        c.setFuture(simulationExecutor.submit(c));

        return new Pair<>(simulation.getName(), c);
    }

    SimulationResult convert(Simulation simulation, String simcJSON) {
        if (simcJSON.isEmpty()) return null;

        SimulationResult simulationResult = new SimulationResult();
        simulationResult.profileResultList = new ArrayList<>();
        simulationResult.simName = simulation.getName();
        simulationResult.wowClass = simulation.getWowClass();
        simulationResult.wowSpec = simulation.getWowSpec();
        try {
            //Convert simcJSON into SimulationResult
            SimcOutputJSON simcOutputJSON = JsonParser.fromJson(simcJSON, SimcOutputJSON.class);
            simcOutputJSON.sim.players.forEach(player -> {
                ProfileResult profileResult = new ProfileResult(player.name, player.collected_data.dps.mean, player.collected_data.dps.max, player.collected_data.dps.min);
                simulationResult.profileResultList.add(profileResult);
            });
            simcOutputJSON.sim.profilesets.results.forEach(profileset -> {
                ProfileResult profileResult = new ProfileResult(profileset.name, profileset.mean, profileset.max, profileset.min);
                simulationResult.profileResultList.add(profileResult);
            });
        } catch (JsonProcessingException ex) {
            LOGGER.error("Could not parse Simc JSON for: ".concat(simulation.getName()), ex);
        }

        return simulationResult;
    }
}
