package org.rednero.deadbydaylight.utils.lists;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.rednero.deadbydaylight.utils.enums.GameState;
import org.rednero.deadbydaylight.utils.enums.PlayerType;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlayerList {
    private final FileConfiguration config;
    private final HashMap<Player, PlayerType> playersType;
    private Random random;

    public PlayerList(FileConfiguration config) {
        this.config = config;
        this.playersType = new HashMap<>();
        this.random = new Random(System.currentTimeMillis());
    }

    public HashMap<Player, PlayerType> getPlayersType() {
        return this.playersType;
    }

    public PlayerType getPlayerType(Player player) {
        return this.playersType.get(player);
    }

    public void addPlayer(Player player, PlayerType playerType) {
        this.playersType.put(player, playerType);
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.joinedMessage"));
    }

    public void removePlayer(Player player) {
        this.playersType.remove(player);
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.leftMessage"));
    }

    public int getPlayersCount() {
        int count = 0;
        for (Player player : this.playersType.keySet()) {
            if (this.playersType.get(player) != PlayerType.SPECTATOR) {
                count++;
            }
        }
        return count;
    }

    public int getSurvivorsCount() {
        int survivorsCount = 0;
        for (Player player : this.playersType.keySet()) {
            if (this.playersType.get(player) == PlayerType.SURVIVOR) {
                survivorsCount++;
            }
        }
        return survivorsCount;
    }

    public boolean isPlayerInLobby(Player player) {
        return this.playersType.get(player) != null;
    }

    public void startGame() {
        int current = 0;
        int randomNum = this.random.nextInt(this.getPlayersCount());
        for (Player player : this.playersType.keySet()) {
            if (this.playersType.get(player) != PlayerType.SPECTATOR) {
                if (current == randomNum) {
                    this.playersType.replace(player, PlayerType.KILLER);
                } else {
                    this.playersType.replace(player, PlayerType.SURVIVOR);
                }
                current++;
            }
        }
    }

    public void resetGame() {
        for (Player player : this.playersType.keySet()) {
            if (this.playersType.get(player) != PlayerType.SPECTATOR) {
                this.playersType.replace(player, PlayerType.LOBBY);
            }
        }
    }

    public void sendMessages(String message) {
        for (Player player : this.playersType.keySet()) {
            player.sendMessage(message);
        }
    }
}
