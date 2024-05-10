package com.github.mcengine;

public class GitHub {
    private static final String API_URL = "https://api.github.com/repos/%s/%s/releases/latest";
    private static final String DOWNLOAD_URL_PREFIX = "https://github.com/%s/%s/releases/download/%s/";

    public void downloadFile(String owner, String repo, String fileName) throws IOException {
        // 1. Get the latest release information
        String latestReleaseUrl = String.format(API_URL, owner, repo);
        URL url = new URL(latestReleaseUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "MyJavaApp");

        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to get latest release: " + connection.getResponseMessage());
        }

        String response = new String(connection.getInputStream().readAllBytes());
        JSONObject releaseJson = new JSONObject(response);

        // 2. Extract download URL for the specific file
        String downloadUrl = null;
        JSONArray assets = releaseJson.optJSONArray("assets");
        if (assets != null) {
            for (int i = 0; i < assets.length(); i++) {
                JSONObject asset = assets.getJSONObject(i);
                String assetName = asset.getString("name");
                if (assetName.equals(fileName)) {
                    downloadUrl = String.format(DOWNLOAD_URL_PREFIX, owner, repo, releaseJson.getString("tag_name"), assetName);
                    break;
                }
            }
        }

        if (downloadUrl == null) {
            throw new IOException("File not found in the latest release: " + fileName);
        }

        // 3. Download the file
        connection = (HttpURLConnection) new URL(downloadUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "MyJavaApp");

        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to download file: " + connection.getResponseMessage());
        }

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            outputStream.write(connection.getInputStream().readAllBytes());
        }

        System.out.println("File downloaded successfully: " + fileName);
    }
}