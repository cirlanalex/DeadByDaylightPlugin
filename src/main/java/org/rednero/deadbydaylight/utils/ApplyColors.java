package org.rednero.deadbydaylight.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ApplyColors {
    private final FileConfiguration config;
    public ApplyColors(FileConfiguration config) {
        this.config = config;
        this.applyColorsToAll();
    }

    public void applyColorCodes(String key) {
        String message = this.config.getString(key);
        if (message != null) {
            String colorizedMessage = ChatColor.translateAlternateColorCodes('&', message);
            this.config.set(key, colorizedMessage);
        }
    }

    public void applyColorCodesList(String key) {
        List<String> messages = this.config.getStringList(key);
        if (messages != null && !messages.isEmpty()) {
            List<String> colorizedMessages = new ArrayList<>();
            for (String message : messages) {
                colorizedMessages.add(ChatColor.translateAlternateColorCodes('&', message));
            }
            this.config.set(key, colorizedMessages);
        }
    }

    private void applyColorsToAll() {
        this.applyColorCodes("messages.prefix");
        this.applyColorCodes("messages.joinedMessage");
        this.applyColorCodes("messages.leftMessage");
        this.applyColorCodes("messages.showAdminCommands");
        this.applyColorCodes("messages.unknownCommand");
        this.applyColorCodes("messages.unknownAdminCommand");
        this.applyColorCodes("messages.consoleCommand");
        this.applyColorCodes("messages.playerNotFound");
        this.applyColorCodes("messages.notInLobby");
        this.applyColorCodes("messages.alreadyInLobby");
        this.applyColorCodes("messages.lobbyFull");
        this.applyColorCodes("messages.gameAlreadyStarted");
        this.applyColorCodes("messages.typeSignNotSpecified");
        this.applyColorCodes("messages.noPermissionToPlaceSign");
        this.applyColorCodes("messages.noPermissionToBreakSign");
        this.applyColorCodes("messages.noPermissionToUseCommand");
        this.applyColorCodes("messages.noPermission");
        this.applyColorCodes("messages.comingSoon");
        this.applyColorCodes("messages.killerLeft");
        this.applyColorCodes("messages.survivorLeft");
        this.applyColorCodes("messages.noSurvivorsLeft");
        this.applyColorCodesList("messages.helpCommand");
        this.applyColorCodesList("messages.adminHelpCommand");
        this.applyColorCodesList("signs.joinSign");
        this.applyColorCodesList("signs.leaveSign");
        this.applyColorCodesList("signs.statsSign");
    }
}
