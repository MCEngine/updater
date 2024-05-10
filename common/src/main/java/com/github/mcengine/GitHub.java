package com.github.mcengine;

public class GitHub {
    private final String owner;
    private final String repo;
    URL url;

    public GitHub(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public static String getLatestTag(String owner, String repo) throws IOException {
        URL url = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/releases/latest");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new IOException("Error: HTTP code " + connection.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse JSON to extract tag name
        String tagName = parseJsonForTagName(response.toString());
        return tagName;
    }

    public static boolean isNewerVersion(String currentTag, String latestTag) {
        // Implement your version comparison logic here
        // This is a simplified example assuming semantic versioning format (X.Y.Z)
        String[] currentParts = currentTag.split("\\.");
        String[] latestParts = latestTag.split("\\.");
        
        for (int i = 0; i < Math.min(currentParts.length, latestParts.length); i++) {
            int currentVersion = Integer.parseInt(currentParts[i]);
            int latestVersion = Integer.parseInt(latestParts[i]);
            if (latestVersion > currentVersion) {
                return true;
            } else if (latestVersion < currentVersion) {
                return false;
            }
        }
        
        // If versions are identical up to the compared parts, consider them equal
        return false;
    }

    public static String parseJsonForTagName(String json) {
        // This is a simplified parsing example. You can use a JSON parsing library for a more robust solution
        int start = json.indexOf("\"tag_name\": \"") + 14;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}