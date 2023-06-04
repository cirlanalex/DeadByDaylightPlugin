package org.rednero.deadbydaylight.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.rednero.deadbydaylight.game.Game;

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
