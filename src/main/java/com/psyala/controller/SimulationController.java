package com.psyala.controller;

import com.psyala.model.sim.Simulation;
import com.psyala.model.sim.SimulationResult;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationController {
    private final ExecutorService simulationExecutor = Executors.newFixedThreadPool(3);

    public void shutDown() {
        simulationExecutor.shutdownNow();
    }

  
    public Optional<Pair<String, Future<SimulationResult>>> getSimulationResult(Simulation simulation) {
        try {
            return Optional.of(
                    new Pair<>(
                            simulation.getName(),
                            simulationExecutor.submit(() -> {
                                System.out.println("Running Simulation: " + simulation.getName());

                                //TODO: Random sleep for now, instead of doing anything :)
                                Thread.sleep((ThreadLocalRandom.current().nextInt(5, 21)) * 1000L);

                                return new SimulationResult(simulation.getName(), new ArrayList<>(), "");
                            })
                    )
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}
