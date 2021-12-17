package com.psyala.model.sim;


import java.util.List;

public class SimulationResult {
    private final String name;
    private final List<ProfileResult> profileResultList;
    private final String url;

    public SimulationResult(String name, List<ProfileResult> profileResultList, String url) {
        this.name = name;
        this.profileResultList = profileResultList;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public List<ProfileResult> getProfileResultList() {
        return profileResultList;
    }

    public String getUrl() {
        return url;
    }
}
