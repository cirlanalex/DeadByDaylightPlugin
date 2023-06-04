package org.rednero.deadbydaylight.utils.lists;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import org.rednero.deadbydaylight.utils.enums.GameState;
import org.rednero.deadbydaylight.utils.enums.PlayerType;
import org.rednero.deadbydaylight.utils.structs.SpawnpointEntity;

import java.util.*;

public class PlayerList {
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final FileConfiguration scoreboardConfig;
    private final SpawnpointList spawnpoints;
    private final HashMap<Player, PlayerType> playersType;
    private final HashMap<Player, Scoreboard> scoreboards;
    private final ScoreboardManager scoreboardManager;
    private Random random;
    private static final List<ChatColor> colors = Arrays.asList(ChatColor.values());

    public PlayerList(JavaPlugin plugin, FileConfiguration config, FileConfiguration scoreboardConfig, SpawnpointList spawnpoints) {
        this.plugin = plugin;
        this.config = config;
        this.scoreboardConfig = scoreboardConfig;
        this.spawnpoints = spawnpoints;
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
        List<SpawnpointEntity> copySurvivors = new ArrayList<>(this.spawnpoints.getSpawnpointsSurvivor());
        for (Player player : this.playersType.keySet()) {
            if (this.playersType.get(player) != PlayerType.SPECTATOR) {
                if (current == randomNum) {
                    this.playersType.replace(player, PlayerType.KILLER);
                    this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.inGameKiller").size());
                    int id = this.random.nextInt(this.spawnpoints.getSpawnpointsKiller().size());
                    player.teleport(this.spawnpoints.getSpawnpointsKiller().get(id).toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
                } else {
                    this.playersType.replace(player, PlayerType.SURVIVOR);
                    this.resetScoreboard(player, this.scoreboardConfig.getStringList("scoreboard.content.inGameSurvivor").size());
                    int id = this.random.nextInt(copySurvivors.size());
                    player.teleport(copySurvivors.get(id).toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
                    copySurvivors.remove(id);
                }
                current++;
            } else {
                player.teleport(this.spawnpoints.getSpawnpointSpectator().toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
            }
        }
    }

    public void resetGame() {
        for (Player player : this.playersType.keySet()) {
            if (this.playersType.get(player) != PlayerType.SPECTATOR) {
                this.playersType.replace(player, PlayerType.LOBBY);
            }
            player.teleport(this.spawnpoints.getSpawnpointSpawn().toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
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
