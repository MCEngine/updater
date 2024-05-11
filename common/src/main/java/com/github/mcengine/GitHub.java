package com.github.mcengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;

public class GitHub {
    public static String getLatestTag(String owner, String repo) throws IOException {
        URI uri;
        try {
            uri = new URI("https", "api.github.com", "/repos/" + owner + "/" + repo + "/tags/latest", null);
        } catch (URISyntaxException e) {
            // Handle URI creation exception
            throw new IOException("Error creating URI: " + e.getMessage());
        }

        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Check response code and handle non-200 status codes
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            String errorMessage = "Error: HTTP code " + responseCode;
            if (responseCode == 404) {
                errorMessage += " (Not Found)"; // Add specific message for 404
            }
            throw new IOException(errorMessage);
        }

        // Read and parse response
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return parseJsonForTagName(response.toString());
        }
    }

    public static String parseJsonForTagName(String json) {
        // This is a simplified parsing example. You can use a JSON parsing library for a more robust solution
        int start = json.indexOf("\"tag_name\": \"") + 14;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    public static void run(String owner, String repo) throws IOException {
        try {
            String tag_current = Util.getCurrentTag();
            String tag_latest = getLatestTag(owner, repo);

            if (Util.isNewerVersion(tag_current, tag_latest)) {
                System.out.println("New version available: " + tag_latest);
            } else {
                System.out.println("No updates available");
            }
        } catch (Exception e) {
            System.out.println("Provider : GitHub");
            System.out.println("Owner : " + owner);
            System.out.println("Repository : " + repo);
            System.out.println(e.getMessage());
            System.out.println("=".repeat(e.getMessage().length()));
        }
    }
}