package pl.olafcio.tedge.service;

import com.google.gson.JsonArray;
import org.jspecify.annotations.NullMarked;
import pl.olafcio.tedge.launcher.Main;

import java.io.IOException;
import java.util.ArrayList;

@NullMarked
public class TedgeService extends Main {
    public static void main(String[] args) throws IOException {
        new TedgeService().commandLine(args);
    }

    @Override
    protected void downloadClientLibraries(JsonArray versionLibs, ArrayList<String> classpathClient) throws IOException {
        new UpdatingMavenController().downloadMavenLibraries(versionLibs, classpathClient);
    }
}
