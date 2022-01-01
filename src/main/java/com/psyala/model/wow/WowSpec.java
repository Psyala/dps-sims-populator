package com.psyala.model.wow;

import com.psyala.util.ResourceLoader;

public enum WowSpec {
    Affliction(ResourceLoader.SPEC_WL_AFFLICTION), Demonology(ResourceLoader.SPEC_WL_DEMONOLOGY), Destruction(ResourceLoader.SPEC_WL_DESTRUCTION);

    private final String resourceFolder;

    WowSpec(String resourceFolder) {
        this.resourceFolder = resourceFolder;
    }

    public String getResourceFolder() {
        return resourceFolder;
    }
}
