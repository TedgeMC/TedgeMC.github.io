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
        for (var mcversion : args) {
            new TedgeService().commandLine(new String[]{ mcversion });

            Files.createDirectories(Path.of("library_overrides"));
            Files.writeString(
                    Path.of("library_overrides/" + mcversion + ".json"),
                    new Gson().toJson(UpdatingMavenController.LIBRARIES)
            );

            UpdatingMavenController.LIBRARIES.clear();
        }
    }

    @Override
    protected void downloadClientLibraries(JsonArray versionLibs, ArrayList<String> classpathClient) throws IOException {
        new UpdatingMavenController().downloadMavenLibraries(versionLibs, classpathClient);
    }
}
