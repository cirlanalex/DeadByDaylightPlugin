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

    public void applyColorCodes(String key, FileConfiguration config) {
        String message = config.getString(key);
        if (message != null) {
            String colorizedMessage = ChatColor.translateAlternateColorCodes('&', message);
            config.set(key, colorizedMessage);
        }
    }

    public void applyColorCodesList(String key, FileConfiguration config) {
        List<String> messages = config.getStringList(key);
        if (messages != null && !messages.isEmpty()) {
            List<String> colorizedMessages = new ArrayList<>();
            for (String message : messages) {
                colorizedMessages.add(ChatColor.translateAlternateColorCodes('&', message));
            }
            config.set(key, colorizedMessages);
        }
    }

    private void applyColorsToAll() {
        this.applyColorCodes("messages.prefix", this.config);
        this.applyColorCodes("messages.joinedMessage", this.config);
        this.applyColorCodes("messages.joinedSpectatorMessage", this.config);
        this.applyColorCodes("messages.leftMessage", this.config);
        this.applyColorCodes("messages.showAdminCommands", this.config);
        this.applyColorCodes("messages.unknownCommand", this.config);
        this.applyColorCodes("messages.unknownAdminCommand", this.config);
        this.applyColorCodes("messages.consoleCommand", this.config);
        this.applyColorCodes("messages.playerNotFound", this.config);
        this.applyColorCodes("messages.notInLobby", this.config);
        this.applyColorCodes("messages.alreadyInLobby", this.config);
        this.applyColorCodes("messages.lobbyFull", this.config);
        this.applyColorCodes("messages.gameAlreadyStarted", this.config);
        this.applyColorCodes("messages.typeSignNotSpecified", this.config);
        this.applyColorCodes("messages.noPermissionToPlaceSign", this.config);
        this.applyColorCodes("messages.noPermissionToBreakSign", this.config);
        this.applyColorCodes("messages.noPermissionToUseCommand", this.config);
        this.applyColorCodes("messages.noPermission", this.config);
        this.applyColorCodes("messages.comingSoon", this.config);
        this.applyColorCodes("messages.killerLeft", this.config);
        this.applyColorCodes("messages.survivorLeft", this.config);
        this.applyColorCodes("messages.noSurvivorsLeft", this.config);
        this.applyColorCodesList("messages.helpCommand", this.config);
        this.applyColorCodesList("messages.adminHelpCommand", this.config);
        this.applyColorCodesList("signs.joinSign", this.config);
        this.applyColorCodesList("signs.leaveSign", this.config);
        this.applyColorCodesList("signs.statsSign", this.config);
        this.applyColorCodesList("signs.spectateSign", this.config);
    }
}
