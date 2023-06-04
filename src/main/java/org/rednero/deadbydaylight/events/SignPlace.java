package org.rednero.deadbydaylight.events;

import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.rednero.deadbydaylight.utils.lists.SignList;
import org.rednero.deadbydaylight.utils.enums.SignType;

public class SignPlace implements Listener {
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final SignList signs;

    public SignPlace(JavaPlugin plugin, FileConfiguration config, SignList signs) {
        this.plugin = plugin;
        this.config = config;
        this.signs = signs;
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent event) {
        if (event.getBlock().getState() instanceof Sign) {
            if (event.getLine(0).equalsIgnoreCase("[DeadByDaylight]") || event.getLine(0).equalsIgnoreCase("[DBD]")) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                if (!player.hasPermission("deadbydaylight.admin")) {
                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermissionToPlaceSign"));
                    return;
                }
                if (event.getLine(1).isEmpty()) {
                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.typeSignNotSpecified"));
                    return;
                }
                if (!event.getLine(1).equalsIgnoreCase("join") && !event.getLine(1).equalsIgnoreCase("leave") && !event.getLine(1).equalsIgnoreCase("stats") && !event.getLine(1).equalsIgnoreCase("spectate")) {
                    player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.typeSignNotSpecified"));
                    return;
                }
                Sign sign = (Sign) event.getBlock().getState();
                if (event.getLine(1).equalsIgnoreCase("join")) {
                    int i = 0;
                    for (String line : this.config.getStringList("signs.joinSign")) {
                        sign.setLine(i, line);
                        i++;
                    }
                    this.signs.addSign(sign, SignType.JOIN);
                } else if (event.getLine(1).equalsIgnoreCase("leave")) {
                    int i = 0;
                    for (String line : this.config.getStringList("signs.leaveSign")) {
                        sign.setLine(i, line);
                        i++;
                    }
                    this.signs.addSign(sign, SignType.LEAVE);
                } else if (event.getLine(1).equalsIgnoreCase("stats")) {
                    int i = 0;
                    for (String line : this.config.getStringList("signs.statsSign")) {
                        sign.setLine(i, line);
                        i++;
                    }
                    this.signs.addSign(sign, SignType.STATS);
                } else if (event.getLine(1).equalsIgnoreCase("spectate")) {
                    int i = 0;
                    for (String line : this.config.getStringList("signs.spectateSign")) {
                        sign.setLine(i, line);
                        i++;
                    }
                    this.signs.addSign(sign, SignType.SPECTATE);
                }
                sign.update();
            }
        }
    }
}
