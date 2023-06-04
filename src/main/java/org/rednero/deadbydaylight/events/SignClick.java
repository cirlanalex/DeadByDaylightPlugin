package org.rednero.deadbydaylight.events;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.rednero.deadbydaylight.game.Game;

public class SignClick implements Listener {
    private final Game game;

    public SignClick(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Player player = event.getPlayer();
                if (event.getClickedBlock().hasMetadata("dbd_join")) {
                    this.game.joinGame(player);
                } else if (event.getClickedBlock().hasMetadata("dbd_leave")) {
                    this.game.leaveGame(player);
                } else if (event.getClickedBlock().hasMetadata("dbd_stats")) {
                    this.game.showStats(player);
                }
            }
        }
    }
}
