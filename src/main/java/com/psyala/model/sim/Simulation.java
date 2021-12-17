package com.psyala.model.sim;

import com.psyala.util.ResourceLoader;

public class Simulation {
    private final String name;
    private final String simBase;
    private final String simVariants;
    private final String simOptions;

    public Simulation(String name, String simBase, String simVariants) {
        this(name, simBase, simVariants, ResourceLoader.RSRC_DEFAULT_OPTIONS);
    }

    private Simulation(String name, String simBase, String simVariants, String simOptions) {
        this.name = name;
        this.simBase = simBase;
        this.simVariants = simVariants;
        this.simOptions = simOptions;
    }

    public String getName() {
        return name;
    }

    public String getSimulationBody() {
        return simOptions + "\r\n\r\n" + simBase + "\r\n\r\n" + simVariants;
    }
}
