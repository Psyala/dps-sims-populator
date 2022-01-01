package com.psyala;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.StatusManager;
import com.psyala.controller.ConfigurationController;
import com.psyala.controller.DebugOutputController;
import com.psyala.controller.SimulationController;
import com.psyala.controller.SimulationCraftController;
import com.psyala.model.ConcurrentTask;
import com.psyala.model.Run;
import com.psyala.model.sim.Simulation;
import com.psyala.model.sim.SimulationResult;
import com.psyala.model.wow.WowClass;
import com.psyala.model.wow.WowSpec;
import com.psyala.util.ResourceLoader;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DpsSimPopulator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DpsSimPopulator.class);

    public static void main(String[] args) {
        //First things first... init logging
        try {
            initLogging();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        //Initialise Controllers
        LOGGER.info("Starting...");
        ConfigurationController configurationController;
        try {
            configurationController = new ConfigurationController();
        } catch (Exception ex) {
            LOGGER.error("Could not init configuration ... exiting", ex);
            return;
        }
        DebugOutputController debugOutputController = new DebugOutputController(configurationController);
        SimulationCraftController simulationCraftController = new SimulationCraftController(configurationController, debugOutputController);
        SimulationController simulationController = new SimulationController(configurationController, simulationCraftController);

        //Do Process
        try {
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
            LOGGER.info(getSimulationStatus(simulationResults));

            //Create Run Object
            Run run = new Run();
            run.runTime = start.toInstant().toString();
            run.simcFullVersion = simcVersionInfo.get();
            run.simcGitRevision = getSimcGitRevision(simcVersionInfo.get());
            run.simulationResultList = simulationResults.stream()
                    .map(Pair::getValue)
                    .map(ConcurrentTask::getFuture)
                    .filter(Future::isDone)
                    .map(optionalFuture -> {
                        try {
                            return optionalFuture.get();
                        } catch (Exception ex) {
                            return Optional.empty();
                        }
                    })
                    .filter(Optional::isPresent)
                    .map(o -> (SimulationResult) o.get())
                    .collect(Collectors.toList());

            //Store run details in output dir
            simulationController.getSimulationCraftController().storeRun(start, run);

            //Tidy Up
            simulationController.shutDown();
            LOGGER.info("Complete");
        } catch (Exception ex) {
            simulationController.shutDown();
            LOGGER.error("Error during RunTime", ex);
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
                new Simulation("Affliction_Talent_ST", WowClass.Warlock, WowSpec.Affliction, ResourceLoader.TALENT_COMPARISON, ResourceLoader.FIGHT_OPTIONS_ST),
                new Simulation("Demonology_Talent_ST", WowClass.Warlock, WowSpec.Demonology, ResourceLoader.TALENT_COMPARISON, ResourceLoader.FIGHT_OPTIONS_ST),
                new Simulation("Destruction_Talent_ST", WowClass.Warlock, WowSpec.Destruction, ResourceLoader.TALENT_COMPARISON, ResourceLoader.FIGHT_OPTIONS_ST)
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

    static String getSimcGitRevision(String simcVersionInfo) {
        //SimulationCraft 915-01 for World of Warcraft 9.1.5.41359 Live (hotfix 2021-12-14/41359, git build shadowlands f0b29f0)
        //can get github hash: https://github.com/simulationcraft/simc/commits/f0b29f0
        Pattern regexPattern = Pattern.compile("(git build[\\s\\w]*\\s)(\\w+)");
        Matcher matcher = regexPattern.matcher(simcVersionInfo);
        if (matcher.find()) return matcher.group(2);
        return "N/A";
    }
}
