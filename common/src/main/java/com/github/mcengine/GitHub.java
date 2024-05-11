package com.github.mcengine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;

public class GitHub {
    public static String getLatestTag(String owner, String repo) throws Exception {
        String urlString = "https://api.github.com/repos/" + owner + "/" + repo + "/tags";

        URI uri = new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to retrieve tags! HTTP error code: " + connection.getResponseCode());
        }

        StringBuilder responseContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
        }

        JSONArray tags = new JSONArray(responseContent.toString());

        // Find the latest tag based on creation date (assuming annotated tags)
        String latestTagName = null;
        for (int i = 0; i < tags.length(); i++) {
            JSONObject tag = tags.getJSONObject(i);
            if (latestTagName == null || tag.has("tagger") && tag.getLong("created_at") > new JSONObject(latestTagName).getLong("created_at")) {
                latestTagName = tag.toString();
            }
        }

        if (latestTagName != null) {
            return new JSONObject(latestTagName).getString("name");
        } else {
            return null;
        }
    }
}