package com.github.mcengine;

import java.util.List;
import java.util.ArrayList;
import java.lang.Class;
import java.lang.reflect.Method;

import org.bukkit.plugin.java.JavaPlugin;

public class MCEngineUpdater extends JavaPlugin {
    @Override
    public void onEnable() {
        // Scheduled task to run every sunday 00:00AM
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    String[] providers = {"GitHub"};
                    for (String provider: providers) {
                        Class<?> clazz = Class.forName("com.github.mcengine." + provider);

                        List<String> datas = new ArrayList<>();
                        datas = Util.readFile(provider);
                        for (String data: datas) {
                            String[] parts = data.split(":");

                            // Data format: owner:repo:tag
                            String owner = parts[0];
                            String repo = parts[1];
                            String tag_current = parts[2];

                            // Get latest tag from Provider
                            String tag_latest = (String) clazz.getMethod("getLatestTag", String.class, String.class).invoke(null, owner, repo);

                            if (Util.isNewerVersion(tag_current, tag_latest)) {
                                // Notify user about new version
                                System.out.println("Owner : " + owner);
                                System.out.println("Repository : " + repo);
                                System.out.println("Current version : " + tag_current);
                                System.out.println("Latest version : " + tag_latest);
                                System.out.println("=".repeat(10));
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }     
            }
        }, 0L, 20L * 60);
    }

    @Override
    public void onDisable() {}
}