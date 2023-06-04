package org.rednero.deadbydaylight.utils.lists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.rednero.deadbydaylight.utils.enums.GameState;
import org.rednero.deadbydaylight.utils.enums.PlayerType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlayerList {
    private final FileConfiguration config;
    private final FileConfiguration scoreboardConfig;
    private final HashMap<Player, PlayerType> playersType;
    private final HashMap<Player, Scoreboard> scoreboards;
    private final ScoreboardManager scoreboardManager;
    private Random random;
    private static final List<ChatColor> colors = Arrays.asList(ChatColor.values());

    public PlayerList(FileConfiguration config, FileConfiguration scoreboardConfig) {
        this.config = config;
        this.scoreboardConfig = scoreboardConfig;
        this.playersType = new HashMap<>();
        this.scoreboards = new HashMap<>();
        this.scoreboardManager = Bukkit.getScoreboardManager();
        this.random = new Random(System.currentTimeMillis());
    }

    public HashMap<Player, PlayerType> getPlayersType() {
        return this.playersType;
    }

    public PlayerType getPlayerType(Player player) {
        return this.playersType.get(player);
    }

    public void addPlayer(Player player, PlayerType playerType, GameState gameState) {
        this.playersType.put(player, playerType);
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.joinedMessage"));
        Scoreboard scoreboard = this.scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        for (int i = 0; i < 16; i++) {
            Team team = scoreboard.registerNewTeam("team" + i);
            team.addEntry(colors.get(i).toString());
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("");
        player.setScoreboard(scoreboard);
        this.scoreboards.put(player, scoreboard);
        if (gameState == GameState.LOBBY) {
            this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.lobby").size());
        } else {
            this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.starting").size());
        }
    }

    public void addSpectator(Player player, GameState gameState) {
        this.playersType.put(player, PlayerType.SPECTATOR);
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.joinedSpectatorMessage"));
        Scoreboard scoreboard = this.scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        for (int i = 0; i < 16; i++) {
            Team team = scoreboard.registerNewTeam("team" + i);
            team.addEntry(colors.get(i).toString());
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("");
        player.setScoreboard(scoreboard);
        this.scoreboards.put(player, scoreboard);
        if (gameState == GameState.LOBBY) {
            this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.lobby").size());
        } else if (gameState == GameState.STARTING) {
            this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.starting").size());
        } else if (gameState == GameState.INGAME) {
            this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.inGameSpectator").size());
        } else {
            this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.ending").size());
        }
    }

    public void resetScoreboardForAll(GameState gameState) {
        for (Player player : this.playersType.keySet()) {
            if (gameState == GameState.LOBBY) {
                this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.lobby").size());
            } else if (gameState == GameState.STARTING) {
                this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.starting").size());
            } else {
                this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.ending").size());
            }
        }
    }

    private void resetScoreboard(Player player, int lines) {
        Scoreboard scoreboard = this.scoreboards.get(player);
        Objective objective = scoreboard.getObjective("scoreboard");
        for (int i = 0; i < 16; i++) {
            Team team = scoreboard.getTeam("team" + i);
            team.setPrefix("");
            team.setSuffix("");
            scoreboard.resetScores(this.colors.get(i).toString());
        }
        for (int i = 15; i > 15 - lines; i--) {
            objective.getScore(colors.get(i).toString()).setScore(i);
        }
    }

    public void removePlayer(Player player) {
        this.playersType.remove(player);
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        this.scoreboards.remove(player);
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
                    this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.inGameKiller").size());
                } else {
                    this.playersType.replace(player, PlayerType.SURVIVOR);
                    this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.inGameSurvivor").size());
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
            this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.lobby").size());
        }
    }

    public void sendMessages(String message) {
        for (Player player : this.playersType.keySet()) {
            player.sendMessage(message);
        }
    }

    public Scoreboard getScoreboard(Player player) {
        return this.scoreboards.get(player);
    }
}
