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

    private void showStats(Player player, String[] args) {
        if (args.length > 1) {
            Player target = player.getServer().getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.playerNotFound"));
                return;
            }
            this.game.showStats(player, target);
            return;
        }
        this.game.showStats(player);
    }

    private void adminSetCommand(Player player, String[] args) {
        if (args.length == 2) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminSetCommandUsage"));
            return;
        }
        if (this.game.getPlayers().getTotalCount() > 0) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
            return;
        }
        switch (args[2].toLowerCase()) {
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
    }

    private void addItem(Player player, String name, String lore, String replace) {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        player.getInventory().setItem(0, item);
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCreateUsage").replace("%type%", replace));
    }

    private void adminAddCommand(Player player, String[] args) {
        if (args.length == 2) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminAddCommandUsage"));
            return;
        }
        if (this.game.getPlayers().getTotalCount() > 0) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
            return;
        }
        switch (args[2].toLowerCase()) {
            case "generator":
                this.addItem(player, "Add Generator", "dbd.generator", "generator");
                break;
            case "totem":
                this.addItem(player, "Add Totem", "dbd.totem", "totem");
                break;
            case "hatch":
                this.addItem(player, "Add Hatch", "dbd.hatch", "hatch");
                break;
            case "exitgate":
                this.addItem(player, "Add Exit Gate", "dbd.exitgate", "exitgate");
                break;
            case "chest":
                this.addItem(player, "Add Chest", "dbd.chest", "chest");
                break;
            case "hook":
                this.addItem(player, "Add Hook", "dbd.hook", "hook");
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
    }

    private void removeSpawnpoint(Player player, List<GameObject> list, int id, String type) {
        if (list.size() < id) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
            return;
        }
        list.remove(id - 1);
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", type).replace("%id%", String.valueOf(id)));
    }

    private void removeSpawnpointEntity(Player player, List<SpawnpointEntity> list, int id, String type) {
        if (list.size() < id) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
            return;
        }
        list.remove(id - 1);
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveSuccess").replace("%type%", type).replace("%id%", String.valueOf(id)));
    }

    private void adminRemoveCommand(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveCommandUsage"));
            return;
        }
        if (this.game.getPlayers().getTotalCount() > 0) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
            return;
        }
        int id;
        try {
            id = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveCommandUsage"));
            return;
        }
        if (id < 1) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminRemoveIdNotFound"));
            return;
        }
        switch (args[2].toLowerCase()) {
            case "generator":
                this.removeSpawnpoint(player, this.game.getSpawnpoints().getGenerators(), id, "generator");
                break;
            case "totem":
                this.removeSpawnpoint(player, this.game.getSpawnpoints().getTotems(), id, "totem");
                break;
            case "hatch":
                this.removeSpawnpoint(player, this.game.getSpawnpoints().getHatches(), id, "hatch");
                break;
            case "exitgate":
                this.removeSpawnpoint(player, this.game.getSpawnpoints().getExitGates(), id, "exitgate");
                break;
            case "chest":
                this.removeSpawnpoint(player, this.game.getSpawnpoints().getChests(), id, "chest");
                break;
            case "hook":
                this.removeSpawnpoint(player, this.game.getSpawnpoints().getHooks(), id, "hook");
                break;
            case "killer":
                this.removeSpawnpointEntity(player, this.game.getSpawnpoints().getSpawnpointsKiller(), id, "killer");
                break;
            case "survivor":
                this.removeSpawnpointEntity(player, this.game.getSpawnpoints().getSpawnpointsSurvivor(), id, "survivor");
                break;
            default:
                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownAdminRemoveCommand"));
                break;
        }
    }

    private void printListSpawnpoints(Player player, List<GameObject> list, String type) {
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", type));
        int i = 1;
        for (GameObject object : list) {
            player.sendMessage(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", object.getSpawnpointObject().saveToString().replace("::", ", ")));
            i++;
        }
    }

    private void printListSpawnpointsEntity(Player player, List<SpawnpointEntity> list, String type) {
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.listItemsIntro").replace("%type%", type));
        int i = 1;
        for (SpawnpointEntity entity : list) {
            player.sendMessage(this.config.getString("messages.listItems").replace("%id%", String.valueOf(i)).replace("%spawnpoint%", String.format("%.2f",entity.getX()) + ", " + String.format("%.2f",entity.getY()) + ", " + String.format("%.2f",entity.getZ())));
            i++;
        }
    }

    private void adminListCommand(Player player, String[] args) {
        if (args.length == 2) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.adminListCommandUsage"));
            return;
        }
        switch (args[2].toLowerCase()) {
            case "generators":
                printListSpawnpoints(player, this.game.getSpawnpoints().getGenerators(), "generator");
                break;
            case "totems":
                printListSpawnpoints(player, this.game.getSpawnpoints().getTotems(), "totem");
                break;
            case "hatches":
                printListSpawnpoints(player, this.game.getSpawnpoints().getHatches(), "hatch");
                break;
            case "exitgates":
                printListSpawnpoints(player, this.game.getSpawnpoints().getExitGates(), "exitgate");
                break;
            case "chests":
                printListSpawnpoints(player, this.game.getSpawnpoints().getChests(), "chest");
                break;
            case "hooks":
                printListSpawnpoints(player, this.game.getSpawnpoints().getHooks(), "hook");
                break;
            case "killers":
                printListSpawnpointsEntity(player, this.game.getSpawnpoints().getSpawnpointsKiller(), "killer");
                break;
            case "survivors":
                printListSpawnpointsEntity(player, this.game.getSpawnpoints().getSpawnpointsSurvivor(), "survivor");
                break;
            default:
                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownAdminListCommand"));
                break;
        }
    }

    private void adminCommand(Player player, String[] args) {
        if (!player.hasPermission("deadbydaylight.admin")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermissionToUseCommand"));
            return;
        }
        if (args.length == 1) {
            this.adminHelp(player);
            return;
        }
        switch (args[1].toLowerCase()) {
            case "help":
                this.adminHelp(player);
                break;
            case "set":
                this.adminSetCommand(player, args);
                break;
            case "add":
                this.adminAddCommand(player, args);
                break;
            case "remove":
                this.adminRemoveCommand(player, args);
                break;
            case "list":
                this.adminListCommand(player, args);
                break;
            default:
                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownAdminCommand"));
                break;
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
        switch (args[0].toLowerCase()) {
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
                this.showStats(player, args);
                break;
            case "admin":
                this.adminCommand(player, args);
                break;
            default:
                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.unknownCommand"));
                break;
        }
        return true;
    }
}
