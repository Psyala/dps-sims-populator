package com.psyala.model.sim;

public class ProfileResult {
    private final String name;
    private final double dpsMean;
    private final double dpsMax;
    private final double dpsMin;

    public ProfileResult(String name, double dpsMean, double dpsMax, double dpsMin) {
        this.name = name;
        this.dpsMean = dpsMean;
        this.dpsMax = dpsMax;
        this.dpsMin = dpsMin;
    }

    public String getName() {
        return name;
    }

    public double getDpsMean() {
        return dpsMean;
    }

    public double getDpsMax() {
        return dpsMax;
    }

    public double getDpsMin() {
        return dpsMin;
    }
}
