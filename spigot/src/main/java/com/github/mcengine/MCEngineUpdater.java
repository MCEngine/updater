package com.github.mcengine;

import org.bukkit.plugin.java.JavaPlugin;

public class MCEngineUpdater extends JavaPlugin {
    @Override
    public void onEnable() {
        // Scheduled task to run every sunday 00:00AM
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    GitHub.run("MCEngine", "mcengine"); // Access the static method in a static way
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }, 0L, 20L * 60);
    }

    @Override
    public void onDisable() {}
}