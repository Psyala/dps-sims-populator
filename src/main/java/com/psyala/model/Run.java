package com.psyala.model;

import com.psyala.model.sim.SimulationResult;

import java.util.List;

public class Run {
    public String runTime;
    public String simcFullVersion;
    public String simcGitRevision;
    public List<SimulationResult> simulationResultList;

    public Run() {

    }

    public Run(String runTime, String simcFullVersion, String simcGitRevision, List<SimulationResult> simulationResultList) {
        this.runTime = runTime;
        this.simcFullVersion = simcFullVersion;
        this.simcGitRevision = simcGitRevision;
        this.simulationResultList = simulationResultList;
    }
}
