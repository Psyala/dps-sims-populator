package com.psyala;

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
            SimulationController simulationController = new SimulationController();

            //Get MetaData
            Date start = new Date();
            String simcVersion = "1.2.3";
            System.out.println("Run Time: " + start.toInstant().toString());
            System.out.println("SimC Version: " + simcVersion);

            //Get sims
            List<Pair<String, Future<SimulationResult>>> simulationResults = getSims()
                    .map(simulationController::getSimulationResult)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            //Await for all sims to finish running
            //TODO: Look at CompletableFuture: https://stackoverflow.com/questions/19348248/waiting-on-a-list-of-future
            while (simulationResults.stream().anyMatch(stringFuturePair -> !stringFuturePair.getValue().isDone())) {
                System.out.println("Waiting for simulations to complete...");
                System.out.println(getSimulationStatus(simulationResults));
                Thread.sleep(5000);
            }
            System.out.println("Simulations Finished");
            System.out.println(getSimulationStatus(simulationResults));

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

    private static String getSimulationStatus(List<Pair<String, Future<SimulationResult>>> simulationResults) {
        return simulationResults.stream()
                .map(stringFuturePair -> stringFuturePair.getKey() + ": " + (stringFuturePair.getValue().isDone() ? "Complete" : "In Progress"))
                .collect(Collectors.joining("\r\n"));
    }
}
