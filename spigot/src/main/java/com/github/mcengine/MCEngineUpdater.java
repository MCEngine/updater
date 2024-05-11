package com.github.mcengine;

import java.util.List;
import java.util.ArrayList;

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
                        List<String> datas = new ArrayList<>();
                        datas = Util.readFile(provider);
                        
                        for (String data: datas) {
                            String[] split = data.split(":");
                            String owner = split[0];
                            String repo = split[1];
                            String version = split[2];
                            
                            Class clazz = Class.forName("com.github.mcengine." + provider + "Provider");
                            String tag_latest = (String) clazz.getMethod("getLatestTag").invoke(null, owner, repo);

                            System.out.println("Provider: " + provider);
                            System.out.println("Owner: " + owner);
                            System.out.println("Repo: " + repo);
                            System.out.println("Current version: " + version);
                            System.out.println("Latest version: " + tag_latest);
                            System.out.println("=".repeat(tag_latest.length()));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }               
            }
        }, 0L, 20L * 60);
    }

    @Override
    public void onDisable() {}
}