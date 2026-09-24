package pl.olafcio.tedge.service;

import com.google.gson.JsonObject;
import pl.olafcio.tedge.launcher.endpoints.MavenController;
import pl.olafcio.tedge.launcher.util.Requests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.regex.Pattern;

public final class UpdatingMavenController extends MavenController {
    public static final ArrayList<String> LIBRARIES
                  = new ArrayList<>();

    @Override
    protected void onBeforeVersion(JsonObject verobj, ExecutorService worker) throws IOException {
        var libName = verobj.get("name").getAsString();

        // Library Updating
        if (
                !libName.startsWith("com.mojang") && !libName.startsWith("net.minecraft") &&
                !libName.startsWith("org.apache.logging.log4j:") &&
                !libName.startsWith("org.github.oshi:") &&
                !libName.startsWith("org.lwjgl")
        ) {
            var parts = libName.split(":");
            var metadata = new String(Requests.get(
                    "https://maven-central-eu.storage-download.googleapis.com/maven2/" +
                    parts[0].replace(".", "/") + "/" + parts[1] + "/" + "maven-metadata.xml"
            ), StandardCharsets.UTF_8);

            //TODO Automatic error fixing

//            var versions = Arrays.stream(metadata.split(Pattern.quote("<version>")))
//                                 .skip(1)
//                                 .map(str -> str.split("</version>")[0])
//                                 .toList();

//            LIBRARY_VERSIONS.put(libName, versions);

            var version = metadata.split(Pattern.quote("<release>"))[1]
                                  .split(Pattern.quote("</release>"))[0];

            verobj.addProperty("name", parts[0] + ":" + parts[1] + ":" + version);

            var artifact = verobj.getAsJsonObject("downloads")
                                 .getAsJsonObject("artifact");

            artifact.remove("sha1");
            artifact.remove("size");

            var path = parts[0].replace(".", "/") + "/" + parts[1] + "/" + version + "/" + parts[1] + "-" + version + ".jar";

            artifact.addProperty("path", path);
            artifact.addProperty("url", "https://maven-central-eu.storage-download.googleapis.com/maven2/" + path);

            LIBRARIES.add(verobj.get("name").getAsString());
        }
    }
}
