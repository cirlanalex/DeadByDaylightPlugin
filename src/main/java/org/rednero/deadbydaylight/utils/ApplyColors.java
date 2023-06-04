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
        this.applyColorCodes("messages.noSpawnPointSpawn", this.config);
        this.applyColorCodes("messages.noSpawnPointSpectator", this.config);
        this.applyColorCodes("messages.notEnoughSpawnPointsKiller", this.config);
        this.applyColorCodes("messages.notEnoughSpawnPointsSurvivor", this.config);
        this.applyColorCodes("messages.notEnoughChests", this.config);
        this.applyColorCodes("messages.notEnoughGenerators", this.config);
        this.applyColorCodes("messages.notEnoughHooks", this.config);
        this.applyColorCodes("messages.notEnoughExitGates", this.config);
        this.applyColorCodes("messages.notEnoughTotems", this.config);
        this.applyColorCodes("messages.noHatch", this.config);
        this.applyColorCodes("messages.adminSetCommandUsage", this.config);
        this.applyColorCodes("messages.unknownAdminSetCommand", this.config);
        this.applyColorCodes("messages.adminSetLobbyCommandSuccess", this.config);
        this.applyColorCodes("messages.adminSetSpectatorCommandSuccess", this.config);
        this.applyColorCodes("messages.adminAddCommandUsage", this.config);
        this.applyColorCodes("messages.adminAddCreateUsage", this.config);
        this.applyColorCodes("messages.adminAddKillerCommandSuccess", this.config);
        this.applyColorCodes("messages.adminAddSurvivorCommandSuccess", this.config);
        this.applyColorCodes("messages.generatorAdded", this.config);
        this.applyColorCodes("messages.chestAdded", this.config);
        this.applyColorCodes("messages.hookAdded", this.config);
        this.applyColorCodes("messages.totemAdded", this.config);
        this.applyColorCodes("messages.exitGateAdded", this.config);
        this.applyColorCodes("messages.hatchAdded", this.config);
        this.applyColorCodes("messages.unknownAdminAddCommand", this.config);
        this.applyColorCodes("messages.adminRemoveCommandUsage", this.config);
        this.applyColorCodes("messages.adminRemoveIdNotFound", this.config);
        this.applyColorCodes("messages.adminRemoveSuccess", this.config);
        this.applyColorCodes("messages.unknownAdminRemoveCommand", this.config);
        this.applyColorCodes("messages.adminListCommandUsage", this.config);
        this.applyColorCodes("messages.listItemsIntro", this.config);
        this.applyColorCodes("messages.listItems", this.config);
        this.applyColorCodes("messages.unknownAdminListCommand", this.config);
        this.applyColorCodesList("messages.helpCommand", this.config);
        this.applyColorCodesList("messages.adminHelpCommand", this.config);
        this.applyColorCodesList("signs.joinSign", this.config);
        this.applyColorCodesList("signs.leaveSign", this.config);
        this.applyColorCodesList("signs.statsSign", this.config);
        this.applyColorCodesList("signs.spectateSign", this.config);
    }
}
