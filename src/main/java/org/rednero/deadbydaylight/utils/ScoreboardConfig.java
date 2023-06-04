package org.rednero.deadbydaylight.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ScoreboardConfig {
    private final JavaPlugin plugin;
    private final ApplyColors applyColors;
    private File configFile;
    private FileConfiguration config;

    public ScoreboardConfig(JavaPlugin plugin, ApplyColors applyColors) {
        this.plugin = plugin;
        this.applyColors = applyColors;
        createConfig();
        loadConfig();
    }

    private void createConfig() {
        this.configFile = new File(this.plugin.getDataFolder(), "scoreboard.yml");
        if (!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            this.plugin.saveResource("scoreboard.yml", false);
        }
    }

    private void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.applyColors.applyColorCodesList("scoreboard.title", this.config);
        this.applyColors.applyColorCodesList("scoreboard.content.lobby", this.config);
        this.applyColors.applyColorCodesList("scoreboard.content.starting", this.config);
        this.applyColors.applyColorCodesList("scoreboard.content.inGameKiller", this.config);
        this.applyColors.applyColorCodesList("scoreboard.content.inGameSurvivor", this.config);
        this.applyColors.applyColorCodesList("scoreboard.content.inGameSpectator", this.config);
        this.applyColors.applyColorCodesList("scoreboard.content.ending", this.config);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
