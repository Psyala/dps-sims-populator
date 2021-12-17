package com.psyala.util;

import com.psyala.db.SQLiteConnection;

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
import java.util.concurrent.atomic.AtomicReference;

public class ResourceLoader {
    //Folders
    private static final String RSRC_FOLDER_DEFAULT = "default";
    private static final String RSRC_FOLDER_DEFAULT_PROFILE = RSRC_FOLDER_DEFAULT + "/profile";
    private static final String RSRC_FOLDER_SIMULATIONS = "simulations";
    private static final String RSRC_FOLDER_DB = "db";

    //Specs
    public static final String RSRC_DEFAULT_PROFILE_AFFLICTION = RSRC_FOLDER_DEFAULT_PROFILE + "/affliction.simc";
    public static final String RSRC_DEFAULT_PROFILE_DESTRUCTION = RSRC_FOLDER_DEFAULT_PROFILE + "/destruction.simc";
    public static final String RSRC_DEFAULT_PROFILE_DEMONOLOGY = RSRC_FOLDER_DEFAULT_PROFILE + "/demonology.simc";

    //Defaults
    public static final String RSRC_DEFAULT_OPTIONS = RSRC_FOLDER_DEFAULT + "/options.simc";

    //DB
    public static final String RSRC_DB_RESULTS = RSRC_FOLDER_DB + "/results.db";

    private static Optional<URL> getRawResource(String resourcePath) {
        return Optional.ofNullable(ResourceLoader.class.getClassLoader().getResource(resourcePath));
    }

    public static Optional<SQLiteConnection> initSQLiteConnection(String dbPath) {
        AtomicReference<SQLiteConnection> sqLiteConnection = new AtomicReference<>(null);

        getRawResource(dbPath).ifPresent(url -> {
            String filePath = url.getFile().substring(1);
            sqLiteConnection.set(new SQLiteConnection(filePath));
        });

        return Optional.ofNullable(sqLiteConnection.get());
    }

    private static List<String> getFileContentsList(String resourcePath) {
        List<String> lines = new ArrayList<>();

        getRawResource(resourcePath).ifPresent(url -> {
            try {
                Path path = Paths.get(url.toURI());
                lines.addAll(Files.readAllLines(path, StandardCharsets.UTF_8));
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
            }
        });

        return lines;
    }

    public static String getFileContentsString(String resourcePath) {
        List<String> lines = getFileContentsList(resourcePath);
        return String.join("\r\n", lines);
    }
}
