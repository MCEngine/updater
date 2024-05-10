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
                    List<String> github_datas = new ArrayList<>();
                    github_datas = Util.readFile("GitHub");
                    for (String data: github_datas) {
                        String[] parts = github_datas.split("/");
                        String owner = parts[0];
                        String repo = parts[1];
                        GitHub.run(owner, repo);
                    }
                    List<String> gitlab_datas = new ArrayList<>();
                    gitlab_datas = Util.readFile("GitLab");
                    for (String data: github_datas) {
                        String[] parts = gitlab_datas.split("/");
                        String owner = parts[0];
                        String repo = parts[1];
                        GitLab.run(owner, repo);
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