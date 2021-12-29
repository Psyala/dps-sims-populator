package com.psyala.controller;

import com.psyala.model.sim.Simulation;
import com.psyala.model.sim.SimulationResult;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class SimulationController {
    private final ExecutorService simulationExecutor;
    private final SimulationCraftController simulationCraftController;

    public SimulationController(ConfigurationController configurationController) throws IOException {
        simulationExecutor = Executors.newFixedThreadPool(configurationController.simcThreads);
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

    public Pair<String, Future<Optional<SimulationResult>>> performSim(Simulation simulation) {
        return new Pair<>(
                simulation.getName(),
                simulationExecutor.submit(() -> {
                    System.out.println("Starting Simulation: " + simulation.getName());

                    AtomicReference<SimulationResult> simResult = new AtomicReference<>(null);
                    simulationCraftController.runSimulation(simulation).ifPresent(simcOutput -> {
                        System.out.println(simulation.getName() + " output:\r\n" + simcOutput);
                    });

                    return Optional.ofNullable(simResult.get());
                })
        );
    }

}
