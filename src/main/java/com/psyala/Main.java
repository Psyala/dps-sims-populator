package com.psyala;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.StatusManager;
import com.psyala.controller.ConfigurationController;
import com.psyala.controller.SimulationController;
import com.psyala.model.ConcurrentTask;
import com.psyala.model.sim.Simulation;
import com.psyala.util.ResourceLoader;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        //First things first... init logging
        try {
            initLogging();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        //Do Process
        try {
            LOGGER.info("Starting...");
            ConfigurationController configurationController;
            try {
                configurationController = new ConfigurationController();
            } catch (Exception ex) {
                LOGGER.error("Could not init configuration ... exiting", ex);
                return;
            }
            SimulationController simulationController = new SimulationController(configurationController);

            //Get MetaData
            Date start = new Date();
            LOGGER.info("Run Time: " + start.toInstant().toString());
            Optional<String> simcVersionInfo = simulationController.getSimulationCraftController().getSimcVersionInfo();
            {
                if (simcVersionInfo.isPresent()) {
                    LOGGER.info("SimC Version Info: " + simcVersionInfo.get());
                } else {
                    LOGGER.error("Could not get SimC Version Info");
                    return;
                }
            }

            //Get sims
            List<Pair<String, ConcurrentTask>> simulationResults = getSims()
                    .map(simulationController::performSim)
                    .collect(Collectors.toList());

            //Await for all sims to finish running
            while (simulationResults.stream().anyMatch(stringFuturePair -> !stringFuturePair.getValue().isDone())) {
                LOGGER.info("Waiting for simulations to complete...\r\n".concat(getSimulationStatus(simulationResults)));
                Thread.sleep(5000);
            }
            LOGGER.info("Simulations Finished");
            System.out.println(getSimulationStatus(simulationResults));

            //TODO: Report any sims which didn't produce a valid result

            simulationController.shutDown();
            LOGGER.info("Complete");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void initLogging() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusManager statusManager = lc.getStatusManager();
        OnConsoleStatusListener onConsoleListener = new OnConsoleStatusListener();
        statusManager.add(onConsoleListener);
    }

    private static Stream<Simulation> getSims() {
        return Stream.of(
                new Simulation("Affliction_Base_ST", ResourceLoader.SPEC_WL_AFFLICTION, "", ResourceLoader.FIGHT_OPTIONS_ST),
                new Simulation("Demonology_Base_ST", ResourceLoader.SPEC_WL_DEMONOLOGY, "", ResourceLoader.FIGHT_OPTIONS_ST),
                new Simulation("Destruction_Base_ST", ResourceLoader.SPEC_WL_DESTRUCTION, "", ResourceLoader.FIGHT_OPTIONS_ST)
        );
    }

    private static String getSimulationStatus(List<Pair<String, ConcurrentTask>> simulationResults) {
        return simulationResults.stream()
                .map(stringConcurrentTaskPair -> {
                    if (stringConcurrentTaskPair.getValue().isDone())
                        return stringConcurrentTaskPair.getKey() + ": Completed";
                    if (stringConcurrentTaskPair.getValue().isInProgress())
                        return stringConcurrentTaskPair.getKey() + ": In Progress";
                    return stringConcurrentTaskPair.getKey() + ": Not Started";
                })
                .collect(Collectors.joining("\r\n"));
    }
}
