package org.rednero.deadbydaylight.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.rednero.deadbydaylight.game.Game;

public class PlayerLeave implements Listener {
    private final Game game;

    public PlayerLeave(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (this.game.getPlayers().isPlayerInLobby(event.getPlayer())) {
            this.game.playerDisconnect(event.getPlayer());
        }
    }
}
