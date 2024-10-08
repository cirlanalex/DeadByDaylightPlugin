package org.rednero.deadbydaylight.game;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.rednero.deadbydaylight.utils.enums.GameState;
import org.rednero.deadbydaylight.utils.enums.PlayerType;
import org.rednero.deadbydaylight.utils.lists.PlayerList;
import org.rednero.deadbydaylight.utils.lists.SignList;
import org.rednero.deadbydaylight.utils.lists.SpawnpointList;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final FileConfiguration scoreboardConfig;
    private PlayerList players;
    private SignList signs;
    private SpawnpointList spawnpoints;
    private GameState gameState;
    private int countdownTaskId;
    private int particleTaskId = -1;
    private int countdown;
    private List<Player> showParticleList = new ArrayList<>();

    public Game(JavaPlugin plugin, FileConfiguration config, FileConfiguration scoreboardConfig) {
        this.plugin = plugin;
        this.config = config;
        this.scoreboardConfig = scoreboardConfig;
        this.signs = new SignList(plugin);
        this.spawnpoints = new SpawnpointList(plugin);
        this.players = new PlayerList(this.plugin, this.config, this.scoreboardConfig, this.spawnpoints);
        this.gameState = GameState.LOBBY;
    }

    private void startingGame() {
        this.gameState = GameState.STARTING;
        this.players.resetScoreboardForAll(this.gameState);
        this.countdown = this.config.getInt("game.waitingForPlayersTime");
        this.startGameCountdown();
    }

    private void cancelStartingGame() {
        this.gameState = GameState.LOBBY;
        this.players.resetScoreboardForAll(this.gameState);
        Bukkit.getScheduler().cancelTask(this.countdownTaskId);
    }

    private void startGame() {
        this.gameState = GameState.INGAME;
        this.players.startGame();
    }

    private void endGame() {
        this.gameState = GameState.ENDING;
        this.players.resetScoreboardForAll(this.gameState);
        this.countdown = this.config.getInt("game.endingTime");
        this.resetGameCountdown();
    }

    private void resetGame() {
        Bukkit.getScheduler().cancelTask(this.countdownTaskId);
        this.gameState = GameState.LOBBY;
        this.players.resetGame();
        if (this.players.getPlayersCount() >= this.config.getInt("game.minPlayers")) {
            this.startingGame();
        }
    }

    private void killerLeaveGame(Player player) {
        this.players.removePlayer(player);
        this.players.sendMessages(this.config.getString("messages.prefix") + this.config.getString("messages.killerLeft").replace("%killer%", player.getName()));
        this.endGame();
    }

    private void survivorLeaveGame(Player player) {
        this.players.removePlayer(player);
        this.players.sendMessages(this.config.getString("messages.prefix") + this.config.getString("messages.survivorLeft").replace("%survivor%", player.getName()));
        if (this.players.getSurvivorsCount() == 0) {
            this.players.sendMessages(this.config.getString("messages.prefix") + this.config.getString("messages.noSurvivorsLeft"));
            this.endGame();
        }
    }

    private void resetGameCountdown() {
        this.countdownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            if (this.countdown == 1) {
                this.resetGame();
            } else {
                this.countdown--;
            }
        }, 0L, 20L);
    }

    private void startGameCountdown() {
        this.countdownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            if (this.countdown == 1) {
                this.startGame();
                Bukkit.getScheduler().cancelTask(this.countdownTaskId);
            } else {
                this.countdown--;
            }
        }, 0L, 20L);
    }

    private boolean checkSpawnpoints(Player player) {
        if (this.spawnpoints.getSpawnpointSpawn() == null) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noSpawnPointSpawn"));
            return false;
        }
        if (this.spawnpoints.getSpawnpointSpectator() == null) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noSpawnPointSpectator"));
            return false;
        }
        if (this.spawnpoints.getSpawnpointsKiller().size() < 1) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.notEnoughSpawnPointsKiller"));
            return false;
        }
        if (this.spawnpoints.getSpawnpointsSurvivor().size() < 4) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.notEnoughSpawnPointsSurvivor"));
            return false;
        }
        if (this.spawnpoints.getChests().size() < this.config.getInt("game.chests")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.notEnoughChests"));
            return false;
        }
        if (this.spawnpoints.getGenerators().size() < this.config.getInt("game.generators")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.notEnoughGenerators"));
            return false;
        }
        if (this.spawnpoints.getHooks().size() < this.config.getInt("game.hooks")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.notEnoughHooks"));
            return false;
        }
        if (this.spawnpoints.getExitGates().size() < this.config.getInt("game.exitGates")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.notEnoughExitGates"));
            return false;
        }
        if (this.spawnpoints.getTotems().size() < this.config.getInt("game.totems")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.notEnoughTotems"));
            return false;
        }
        if (this.config.getBoolean("game.hatch") && this.spawnpoints.getHatches().size() < 1) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noHatch"));
            return false;
        }
        return true;
    }

    public SignList getSigns() {
        return this.signs;
    }

    public void joinGame(Player player) {
        if (!player.hasPermission("deadbydaylight.play")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermission"));
            return;
        }
        if (!this.checkSpawnpoints(player)) {
            return;
        }
        if (this.players.isPlayerInLobby(player)) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.alreadyInLobby"));
            return;
        }
        if (this.players.getPlayersCount() >= 5) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.lobbyFull"));
            return;
        }
        if (this.gameState == GameState.LOBBY) {
            this.players.addPlayer(player, PlayerType.LOBBY, this.gameState);
            player.teleport(this.spawnpoints.getSpawnpointSpawn().toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
            if (this.players.getPlayersCount() == this.config.getInt("game.minPlayers")) {
                this.gameState = GameState.STARTING;
                this.startingGame();
            }
        } else if (this.gameState == GameState.STARTING) {
            this.players.addPlayer(player, PlayerType.LOBBY, this.gameState);
            player.teleport(this.spawnpoints.getSpawnpointSpawn().toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
            if (this.countdown < this.config.getInt("game.fullLobbyTime") || this.players.getPlayersCount() == 5) {
                this.countdown = this.config.getInt("game.fullLobbyTime");
            }
        } else {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.gameAlreadyStarted"));
        }
    }

    public void leaveGame(Player player) {
        if (!player.hasPermission("deadbydaylight.play")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermission"));
            return;
        }
        if (!this.players.isPlayerInLobby(player)) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.notInLobby"));
            return;
        }
        if (this.players.getPlayerType(player) == PlayerType.SPECTATOR) {
            this.players.removePlayer(player);
            player.teleport(this.spawnpoints.getSpawnpointSpawn().toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
            return;
        }
        this.playerDisconnect(player);
    }

    public void spectateGame(Player player) {
        if (!player.hasPermission("deadbydaylight.play")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermission"));
            return;
        }
        if (!this.checkSpawnpoints(player)) {
            return;
        }
        if (this.players.isPlayerInLobby(player)) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.alreadyInLobby"));
            return;
        }
        this.players.addSpectator(player, this.gameState);
        player.teleport(this.spawnpoints.getSpawnpointSpawn().toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
    }

    public void playerDisconnect(Player player) {
        player.teleport(this.spawnpoints.getSpawnpointSpawn().toLocation(this.plugin.getServer().getWorld(this.config.getString("game.world"))));
        if (this.gameState == GameState.LOBBY) {
            this.players.removePlayer(player);
        } else if (this.gameState == GameState.STARTING) {
            this.players.removePlayer(player);
            if (this.players.getPlayersCount() < this.config.getInt("game.minPlayers")) {
                this.cancelStartingGame();
            } else {
                if (this.countdown < this.config.getInt("game.fullLobbyTime")) {
                    this.countdown = this.config.getInt("game.fullLobbyTime");
                }
            }
        } else if (this.gameState == GameState.INGAME) {
            if (this.players.getPlayerType(player) == PlayerType.KILLER) {
                this.killerLeaveGame(player);
            } else if (this.players.getPlayerType(player) == PlayerType.SURVIVOR) {
                this.survivorLeaveGame(player);
            }
        } else {
            this.players.removePlayer(player);
        }
    }

    public void showStats(Player player) {
        if (!player.hasPermission("deadbydaylight.play")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermission"));
            return;
        }
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.comingSoon"));
    }

    public void showStats(Player player, Player target) {
        if (!player.hasPermission("deadbydaylight.play")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermission"));
            return;
        }
        player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.comingSoon"));
    }

    public int getCountdown() {
        return this.countdown;
    }

    public PlayerList getPlayers() {
        return this.players;
    }

    public SpawnpointList getSpawnpoints() {
        return this.spawnpoints;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public List<Player> getShowParticleList() {
        return this.showParticleList;
    }

    public void setParticleTaskId(int taskId) {
        this.particleTaskId = taskId;
    }

    public int getParticleTaskId() {
        return this.particleTaskId;
    }
}
