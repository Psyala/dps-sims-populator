package com.psyala;

import com.psyala.controller.ConfigurationController;
import com.psyala.controller.SimulationController;
import com.psyala.model.sim.Simulation;
import com.psyala.model.sim.SimulationResult;
import com.psyala.util.ResourceLoader;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Starting...");
            ConfigurationController configurationController;
            try {
                configurationController = new ConfigurationController();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Could not init configuration ... exiting");
                return;
            }
            SimulationController simulationController = new SimulationController(configurationController);

            //Get MetaData
            Date start = new Date();
            System.out.println("Run Time: " + start.toInstant().toString());
            Optional<String> simcVersionInfo = simulationController.getSimulationCraftController().getSimcVersionInfo();
            {
                if (simcVersionInfo.isPresent()) {
                    System.out.println("SimC Version Info: " + simcVersionInfo.get());
                } else {
                    System.err.println("Could not get SimC Version Info");
                    return;
                }
            }

            //Get sims
            List<Pair<String, Future<Optional<SimulationResult>>>> simulationResults = getSims()
                    .map(simulationController::performSim)
                    .collect(Collectors.toList());

            //Await for all sims to finish running
            while (simulationResults.stream().anyMatch(stringFuturePair -> !stringFuturePair.getValue().isDone())) {
                System.out.println("Waiting for simulations to complete...");
                System.out.println(getSimulationStatus(simulationResults));
                Thread.sleep(5000);
            }
            System.out.println("Simulations Finished");
            System.out.println(getSimulationStatus(simulationResults));

            //TODO: Report any sims which didn't produce a valid result

            simulationController.shutDown();
            System.out.println("Done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Stream<Simulation> getSims() {
        return Stream.of(
                new Simulation("Affliction", ResourceLoader.RSRC_DEFAULT_PROFILE_AFFLICTION, ""),
                new Simulation("Demonology", ResourceLoader.RSRC_DEFAULT_PROFILE_DEMONOLOGY, ""),
                new Simulation("Destruction", ResourceLoader.RSRC_DEFAULT_PROFILE_DESTRUCTION, "")
        );
    }

    private static String getSimulationStatus(List<Pair<String, Future<Optional<SimulationResult>>>> simulationResults) {
        return simulationResults.stream()
                .map(stringFuturePair -> stringFuturePair.getKey() + ": " + (stringFuturePair.getValue().isDone() ? "Complete" : "In Progress"))
                .collect(Collectors.joining("\r\n"));
    }
}
