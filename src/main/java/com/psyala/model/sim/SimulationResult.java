package com.psyala.model.sim;


import java.util.ArrayList;
import java.util.List;

public class SimulationResult {
    private final String name;
    private final List<ProfileResult> profileResultList;

    public SimulationResult(String name) {
        this.name = name;
        this.profileResultList = new ArrayList<>();
    }

    public SimulationResult(String name, List<ProfileResult> profileResultList) {
        this.name = name;
        this.profileResultList = profileResultList;
    }

    public String getName() {
        return name;
    }

    public List<ProfileResult> getProfileResultList() {
        return profileResultList;
    }

}
