package com.psyala.model.sim;

import com.psyala.model.wow.WowClass;
import com.psyala.model.wow.WowSpec;
import com.psyala.util.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final String name;
    private final WowClass wowClass;
    private final WowSpec wowSpec;
    private final String simVariant;
    private final String fightOptions;

    public Simulation(String name, WowClass wowClass, WowSpec wowSpec, String simVariant, String fightOptions) {
        this.name = name;
        this.wowClass = wowClass;
        this.wowSpec = wowSpec;
        this.simVariant = simVariant;
        this.fightOptions = fightOptions;
    }

    public String getName() {
        return name;
    }

    public WowClass getWowClass() {
        return wowClass;
    }

    public WowSpec getWowSpec() {
        return wowSpec;
    }

    public String getSimulationBody() {
        List<String> simBuilderList = new ArrayList<>();

        //Fight Options
        simBuilderList.add(ResourceLoader.getFileContentsString(fightOptions));

        //Base Profile
        simBuilderList.add(ResourceLoader.getFileContentsString(wowSpec.getResourceFolder().concat("/").concat(ResourceLoader.BASE_PROFILE)));

        //Simulation Variant
        if (!simVariant.isEmpty())
            simBuilderList.add(ResourceLoader.getFileContentsString(wowSpec.getResourceFolder().concat("/").concat(simVariant)));

        return String.join("\r\n\r\n", simBuilderList);
    }
}
