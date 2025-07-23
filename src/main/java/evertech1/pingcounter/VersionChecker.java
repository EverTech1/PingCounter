package evertech1.pingcounter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;


public class VersionChecker {
    private static final Gson gson = new Gson();
    private static final String updateUrl = "https://gist.githubusercontent.com/EverTech1/73f21b840a9309420c0c1b8156cc17e3/raw/updateFabric.json";
    public enum Results {NOT_STARTED, STARTED, FAILED, OLD, UP_TO_DATE, AHEAD};
    public static Results result = Results.NOT_STARTED;
    private static class Versions{
        @SerializedName("1.21.6-latest")
        String latest;
        @SerializedName("1.21.6-lastImportant")
        String lastImportant;
    }

    private static String readUrl() throws Exception {
        BufferedReader reader = null;
        try {
            URL url = URI.create(VersionChecker.updateUrl).toURL();
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    private static Results parseResults(Versions versions){
        String[] current = PingCounter.MOD_VERSION.split("\\.");
        String[] lastImportant = versions.lastImportant.split("\\.");
        int diffMajor = (Integer.parseInt(lastImportant[0])-Integer.parseInt(current[0]));
        int diffMinor = (Integer.parseInt(lastImportant[1])-Integer.parseInt(current[1]));
        int diffPatch = (Integer.parseInt(lastImportant[2])-Integer.parseInt(current[2]));
        if(diffMajor>0) return Results.OLD;
        if(diffMajor<0) return Results.AHEAD;
        if(diffMinor>0) return Results.OLD;
        if(diffMinor<0) return Results.AHEAD;
        if(diffPatch>0) return Results.OLD;
        if(diffPatch<0) return Results.AHEAD;
        return Results.UP_TO_DATE;
    }
    public static void startCheck() {
        if (result != Results.STARTED) {
            PingCounter.LOGGER.info("Started version check");
            result = Results.STARTED;
            Thread checkThread = new Thread(() -> {
                try {
                    Versions versions = gson.fromJson(readUrl(), Versions.class);
                    result = parseResults(versions);
                } catch (Exception e) {
                    PingCounter.LOGGER.error("Failed to check version", e);
                    result = Results.FAILED;
                }
                PingCounter.LOGGER.info("Version check complete: {}", result.toString());
            });
            checkThread.setName("VersionCheck");
            checkThread.start();
        }
    }
}
