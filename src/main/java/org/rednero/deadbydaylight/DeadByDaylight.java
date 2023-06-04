package org.rednero.deadbydaylight;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.rednero.deadbydaylight.commands.DeadByDaylightCommand;
import org.rednero.deadbydaylight.events.*;
import org.rednero.deadbydaylight.game.Game;
import org.rednero.deadbydaylight.timers.UpdateScoreboard;
import org.rednero.deadbydaylight.utils.ApplyColors;

public final class DeadByDaylight extends JavaPlugin {

    private Game game;
    private FileConfiguration config;

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new SignPlace(this, this.config, this.game.getSigns()), this);
        getServer().getPluginManager().registerEvents(new SignClick(this.game), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(this.game), this);
        getServer().getPluginManager().registerEvents(new SignBreak(this.config, this.game.getSigns()), this);
        getServer().getPluginManager().registerEvents(new SupportSignBreak(this.game.getSigns()), this);
    }

    private void implementCommands() {
        getCommand("dbd").setExecutor(new DeadByDaylightCommand(this.config, this.game));
    }

    private void startTimers() {
        BukkitRunnable scoreboardTask = new UpdateScoreboard(this.game);
        scoreboardTask.runTaskTimer(this, 0L, 10L);
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        this.config = getConfig();
        new ApplyColors(this.config);
        this.game = new Game(this, this.config);
        this.registerEvents();
        this.implementCommands();
        this.startTimers();
        getLogger().info("DeadByDaylight plugin by RedNero has been enabled.");
    }

    @Override
    public void onDisable() {
        this.game.getSigns().saveConfig();
        getLogger().info("DeadByDaylight plugin by RedNero has been disabled.");
    }
}
