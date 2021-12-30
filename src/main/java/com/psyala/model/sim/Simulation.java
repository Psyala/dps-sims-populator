package com.psyala.model.sim;

import com.psyala.util.ResourceLoader;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final String name;
    private final String specFolder;
    private final String simVariant;
    private final String fightOptions;

    public Simulation(String name, String specFolder, String simVariant, String fightOptions) {
        this.name = name;
        this.specFolder = specFolder;
        this.simVariant = simVariant;
        this.fightOptions = fightOptions;
    }

    public String getName() {
        return name;
    }

    public String getSimulationBody() {
        List<String> simBuilderList = new ArrayList<>();

        //Fight Options
        simBuilderList.add(ResourceLoader.getFileContentsString(fightOptions));

        //Base Profile
        simBuilderList.add(ResourceLoader.getFileContentsString(specFolder.concat("/").concat(ResourceLoader.BASE_PROFILE)));

        //Simulation Variant
        if (!simVariant.isEmpty())
            simBuilderList.add(ResourceLoader.getFileContentsString(specFolder.concat("/").concat(simVariant)));

        return String.join("\r\n\r\n", simBuilderList);
    }
}
