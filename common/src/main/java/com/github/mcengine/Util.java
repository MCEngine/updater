package com.github.mcengine;

import java.util.List;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Util {
    public static String getCurrentTag() {
        return "1.0.0";
    }

    public static String getPath() {
        return System.getProperty("user.dir") + "/plugins/Updater";
    }

    public static void createFile(File file) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
    }

    public static List<String> readFile(String provider) {
        List<String> lines = new ArrayList<>();
        File file = new File(getPath() + "/" + provider + ".properties");
        if (!file.exists()) {
            // Create file with createFile() method give github gitlab .properties
            createFile(file);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return lines;
    }
}