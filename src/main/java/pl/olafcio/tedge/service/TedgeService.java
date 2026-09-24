package pl.olafcio.tedge.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.jspecify.annotations.NullMarked;
import pl.olafcio.tedge.launcher.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@NullMarked
public class TedgeService extends Main {
    public static void main(String[] args) throws IOException {
        new TedgeService().commandLine(args);

        Files.writeString(
                Path.of("library_overrides.json"),
                new Gson().toJson(UpdatingMavenController.LIBRARIES)
        );
    }

    @Override
    protected void downloadClientLibraries(JsonArray versionLibs, ArrayList<String> classpathClient) throws IOException {
        new UpdatingMavenController().downloadMavenLibraries(versionLibs, classpathClient);
    }
}
