package com.github.mcengine;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.mcegnine.Util;

public class MCEngineUpdater extends JavaPlugin {

    @Override
    public void onEnable() {
        // Scheduled task to run every sunday 00:00AM
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Util.readfile("github");
            }
        }, 0L, 20L * 60);
    }

    @Override
    public void onDisable() {}
}