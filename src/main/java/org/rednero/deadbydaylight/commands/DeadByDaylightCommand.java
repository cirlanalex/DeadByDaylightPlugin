package org.rednero.deadbydaylight.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rednero.deadbydaylight.game.Game;
import org.rednero.deadbydaylight.game.objects.GameObject;
import org.rednero.deadbydaylight.utils.structs.SpawnpointEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeadByDaylightCommand implements CommandExecutor {
    private final FileConfiguration config;
    private final Game game;

    public DeadByDaylightCommand(FileConfiguration config, Game game) {
        this.config = config;
        this.game = game;
    }

    private void help(Player player) {
        for (String message : this.config.getStringList("messages.helpCommand")) {
            player.sendMessage(message);
        }
        if (player.hasPermission("deadbydaylight.admin")) {
            player.sendMessage(this.config.getString("messages.showAdminCommands"));
        }
    }

    private void adminHelp(Player player) {
        for (String message : this.config.getStringList("messages.adminHelpCommand")) {
            player.sendMessage(message);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.consoleCommand"));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("deadbydaylight.play")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermissionToUseCommand"));
            return true;
        }
        if (args.length == 0) {
            this.help(player);
            return true;
        }
        switch (args[0]) {
            case "help":
                this.help(player);
                break;
            case "join":
                this.game.joinGame(player);
                break;
            case "spectate":
                this.game.spectateGame(player);
                break;
            case "leave":
                this.game.leaveGame(player);
                break;
            case "stats":
                if (args.length > 1) {
                    Player target = player.getServer().getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.playerNotFound"));
                        break;
                    }
                    this.game.showStats(player, target);
                    break;
                }
                this.game.showStats(player);
                break;
            case "admin":
                if (!player.hasPermission("deadbydaylight.admin")) {
                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermissionToUseCommand"));
                    break;
                }
                if (args.length == 1) {
                    this.adminHelp(player);
                    break;
                }
                switch (args[1]) {
                    case "help":
                        this.adminHelp(player);
                        break;
                    case "set":
                        if (args.length == 2) {
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminSetCommandUsage"));
                            break;
                        }
                        switch (args[2]) {
                            case "lobby":
                                this.game.getSpawnpoints().setSpawnpointSpawn(new SpawnpointEntity(player));
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminSetLobbyCommandSuccess"));
                                break;
                            case "spectator":
                                this.game.getSpawnpoints().setSpawnpointSpectator(new SpawnpointEntity(player));
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminSetSpectatorCommandSuccess"));
                                break;
                            default:
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownAdminSetCommand"));
                                break;
                        }
                        break;
                    case "add":
                        if (args.length == 2) {
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCommandUsage"));
                            break;
                        }
                        ItemStack item = new ItemStack(Material.STICK);
                        ItemMeta meta = item.getItemMeta();
                        switch (args[2]) {
                            case "generator":
                                meta.setDisplayName(ChatColor.GOLD + "Add Generator");
                                meta.setLore(Arrays.asList("dbd.generator"));
                                item.setItemMeta(meta);
                                player.getInventory().setItem(0, item);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCreateUsage").replace("%type%", "generator"));
                                break;
                            case "totem":
                                meta.setDisplayName(ChatColor.GOLD + "Add Totem");
                                meta.setLore(Arrays.asList("dbd.totem"));
                                item.setItemMeta(meta);
                                player.getInventory().setItem(0, item);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCreateUsage").replace("%type%", "totem"));
                                break;
                            case "hatch":
                                meta.setDisplayName(ChatColor.GOLD + "Add Hatch");
                                meta.setLore(Arrays.asList("dbd.hatch"));
                                item.setItemMeta(meta);
                                player.getInventory().setItem(0, item);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCreateUsage").replace("%type%", "hatch"));
                                break;
                            case "exitgate":
                                meta.setDisplayName(ChatColor.GOLD + "Add Exit Gate");
                                meta.setLore(Arrays.asList("dbd.exitgate"));
                                item.setItemMeta(meta);
                                player.getInventory().setItem(0, item);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCreateUsage").replace("%type%", "exitgate"));
                                break;
                            case "chest":
                                meta.setDisplayName(ChatColor.GOLD + "Add Chest");
                                meta.setLore(Arrays.asList("dbd.chest"));
                                item.setItemMeta(meta);
                                player.getInventory().setItem(0, item);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCreateUsage").replace("%type%", "chest"));
                                break;
                            case "hook":
                                meta.setDisplayName(ChatColor.GOLD + "Add Hook");
                                meta.setLore(Arrays.asList("dbd.hook"));
                                item.setItemMeta(meta);
                                player.getInventory().setItem(0, item);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCreateUsage").replace("%type%", "hook"));
                                break;
                            case "killer":
                                this.game.getSpawnpoints().getSpawnpointsKiller().add(new SpawnpointEntity(player));
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddKillerCommandSuccess"));
                                break;
                            case "survivor":
                                this.game.getSpawnpoints().getSpawnpointsSurvivor().add(new SpawnpointEntity(player));
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddSurvivorCommandSuccess"));
                                break;
                            default:
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownAdminAddCommand"));
                                break;
                        }
                        break;
                    case "remove":
                        if (args.length < 4) {
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveCommandUsage"));
                            break;
                        }
                        int id;
                        try {
                            id = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveCommandUsage"));
                            break;
                        }
                        if (id < 1) {
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                            break;
                        }
                        switch (args[2]) {
                            case "generator":
                                if (this.game.getSpawnpoints().getGenerators().size() < id) {
                                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                                    break;
                                }
                                this.game.getSpawnpoints().getGenerators().remove(id - 1);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", "generator").replace("%id%", String.valueOf(id)));
                                break;
                            case "totem":
                                if (this.game.getSpawnpoints().getTotems().size() < id) {
                                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                                    break;
                                }
                                this.game.getSpawnpoints().getTotems().remove(id - 1);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", "totem").replace("%id%", String.valueOf(id)));
                                break;
                            case "hatch":
                                if (this.game.getSpawnpoints().getHatches().size() < id) {
                                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                                    break;
                                }
                                this.game.getSpawnpoints().getHatches().remove(id - 1);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", "hatch").replace("%id%", String.valueOf(id)));
                                break;
                            case "exitgate":
                                if (this.game.getSpawnpoints().getExitGates().size() < id) {
                                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                                    break;
                                }
                                this.game.getSpawnpoints().getExitGates().remove(id - 1);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", "exitgate").replace("%id%", String.valueOf(id)));
                                break;
                            case "chest":
                                if (this.game.getSpawnpoints().getChests().size() < id) {
                                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                                    break;
                                }
                                this.game.getSpawnpoints().getChests().remove(id - 1);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", "chest").replace("%id%", String.valueOf(id)));
                                break;
                            case "hook":
                                if (this.game.getSpawnpoints().getHooks().size() < id) {
                                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                                    break;
                                }
                                this.game.getSpawnpoints().getHooks().remove(id - 1);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", "hook").replace("%id%", String.valueOf(id)));
                                break;
                            case "killer":
                                if (this.game.getSpawnpoints().getSpawnpointsKiller().size() < id) {
                                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                                    break;
                                }
                                this.game.getSpawnpoints().getSpawnpointsKiller().remove(id - 1);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", "killer").replace("%id%", String.valueOf(id)));
                                break;
                            case "survivor":
                                if (this.game.getSpawnpoints().getSpawnpointsSurvivor().size() < id) {
                                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
                                    break;
                                }
                                this.game.getSpawnpoints().getSpawnpointsSurvivor().remove(id - 1);
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", "survivor").replace("%id%", String.valueOf(id)));
                                break;
                            default:
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownAdminRemoveCommand"));
                                break;
                        }
                        break;
                    case "list":
                        if (args.length == 2) {
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminListCommandUsage"));
                            break;
                        }
                        List<String> lines = new ArrayList<>();
                        int i = 1;
                        switch (args[2]) {
                            case "generators":
                                for (GameObject generator : this.game.getSpawnpoints().getGenerators()) {
                                    lines.add(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", generator.getSpawnpointObject().saveToString().replace("::", ",")));
                                    i++;
                                }
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", "generator"));
                                for (String line : lines) {
                                    player.sendMessage(line);
                                }
                                break;
                            case "totems":
                                for (GameObject totem : this.game.getSpawnpoints().getTotems()) {
                                    lines.add(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", totem.getSpawnpointObject().saveToString().replace("::", ",")));
                                    i++;
                                }
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", "totem"));
                                for (String line : lines) {
                                    player.sendMessage(line);
                                }
                                break;
                            case "hatches":
                                for (GameObject hatch : this.game.getSpawnpoints().getHatches()) {
                                    lines.add(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", hatch.getSpawnpointObject().saveToString().replace("::", ",")));
                                    i++;
                                }
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", "hatch"));
                                for (String line : lines) {
                                    player.sendMessage(line);
                                }
                                break;
                            case "exitgates":
                                for (GameObject exitGate : this.game.getSpawnpoints().getExitGates()) {
                                    lines.add(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", exitGate.getSpawnpointObject().saveToString().replace("::", ",")));
                                    i++;
                                }
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", "exitgate"));
                                for (String line : lines) {
                                    player.sendMessage(line);
                                }
                                break;
                            case "chests":
                                for (GameObject chest : this.game.getSpawnpoints().getChests()) {
                                    lines.add(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", chest.getSpawnpointObject().saveToString().replace("::", ",")));
                                    i++;
                                }
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", "chest"));
                                for (String line : lines) {
                                    player.sendMessage(line);
                                }
                                break;
                            case "hooks":
                                for (GameObject hook : this.game.getSpawnpoints().getHooks()) {
                                    lines.add(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", hook.getSpawnpointObject().saveToString().replace("::", ",")));
                                    i++;
                                }
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", "hook"));
                                for (String line : lines) {
                                    player.sendMessage(line);
                                }
                                break;
                            case "killers":
                                for (SpawnpointEntity killer : this.game.getSpawnpoints().getSpawnpointsKiller()) {
                                    lines.add(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", killer.getX() + "," + killer.getY() + "," + killer.getZ()));
                                    i++;
                                }
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", "killer"));
                                for (String line : lines) {
                                    player.sendMessage(line);
                                }
                                break;
                            case "survivors":
                                for (SpawnpointEntity survivor : this.game.getSpawnpoints().getSpawnpointsSurvivor()) {
                                    lines.add(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", survivor.getX() + "," + survivor.getY() + "," + survivor.getZ()));
                                    i++;
                                }
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", "survivor"));
                                for (String line : lines) {
                                    player.sendMessage(line);
                                }
                                break;
                            default:
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownAdminListCommand"));
                                break;
                        }
                        break;
                    default:
                        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownAdminCommand"));
                        break;
                }
                break;
            default:
                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownCommand"));
                break;
        }
        return true;
    }
}
