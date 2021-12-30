package com.psyala.controller;

import com.psyala.model.ConcurrentTask;
import com.psyala.model.sim.Simulation;
import com.psyala.model.sim.SimulationResult;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationController.class);
    private final ExecutorService simulationExecutor;
    private final SimulationCraftController simulationCraftController;

    public SimulationController(ConfigurationController configurationController) throws IOException {
        simulationExecutor = Executors.newFixedThreadPool(configurationController.simcThreads, r -> new Thread(r, "Simulation Thread"));
        simulationCraftController = new SimulationCraftController(configurationController);
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
        ConcurrentTask c = new ConcurrentTask(() -> convert(simulation.getName(), simulationCraftController.runSimulation(simulation)));
        c.setFuture(simulationExecutor.submit(c));

        return new Pair<>(simulation.getName(), c);
    }

    private SimulationResult convert(String simulationName, String rawSimOutput) {
        if (rawSimOutput.isEmpty()) return null;

        //TODO Convert rawSimOutput into SimulationResult POJO
        return new SimulationResult(simulationName);
    }


}
