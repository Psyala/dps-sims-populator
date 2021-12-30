package com.psyala.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResourceLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);

    //Base Folders
    private static final String FOLDER_FIGHT_OPTIONS = "fight_options";
    private static final String FOLDER_SPEC = "spec";

    //Spec Folders
    public static final String SPEC_WL_AFFLICTION = FOLDER_SPEC + "/wl_affliction";
    public static final String SPEC_WL_DESTRUCTION = FOLDER_SPEC + "/wl_destruction";
    public static final String SPEC_WL_DEMONOLOGY = FOLDER_SPEC + "/wl_demonology";

    //Fight Options
    public static final String FIGHT_OPTIONS_ST = FOLDER_FIGHT_OPTIONS + "/st.simc";

    //Simulations
    public static final String BASE_PROFILE = "base.simc";

    private static Optional<URL> getRawResource(String resourcePath) {
        return Optional.ofNullable(ResourceLoader.class.getClassLoader().getResource(resourcePath));
    }

    private static List<String> getFileContentsList(String resourcePath) {
        List<String> lines = new ArrayList<>();

        getRawResource(resourcePath).ifPresent(url -> {
            try {
                Path path = Paths.get(url.toURI());
                lines.addAll(Files.readAllLines(path, StandardCharsets.UTF_8));
            } catch (URISyntaxException | IOException ex) {
                LOGGER.error("Could not load: " + url, ex);
            }
        });

        return lines;
    }

    public static String getFileContentsString(String resourcePath) {
        List<String> lines = getFileContentsList(resourcePath);
        return String.join("\r\n", lines);
    }
}
