package org.rednero.deadbydaylight.events;

import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.rednero.deadbydaylight.utils.lists.SignList;

public class SignBreak implements Listener {
    private final FileConfiguration config;
    private final SignList signs;

    public SignBreak(FileConfiguration config, SignList signs) {
        this.config = config;
        this.signs = signs;
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if (this.signs.isSign(sign)) {
                if (!event.getPlayer().hasPermission("deadbydaylight.admin")) {
                    event.getPlayer().sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermissionToBreakSign"));
                    event.setCancelled(true);
                    return;
                }
                this.signs.removeSign(sign);
            }

        }
    }
}
