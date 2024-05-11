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
                    String[] providers = {"GitHub", "GitLab"};
                    for (String provider: providers) {
                        List<String> datas = new ArrayList<>();
                        datas = Util.readFile(provider);
                        for (String data: datas) {
                            String[] parts = data.split(":");
                            String owner = parts[0];
                            String repo = parts[1];
                            String tag_current = parts[2];
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