package org.rednero.deadbydaylight.game;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.rednero.deadbydaylight.utils.enums.GameState;
import org.rednero.deadbydaylight.utils.enums.PlayerType;
import org.rednero.deadbydaylight.utils.lists.PlayerList;
import org.rednero.deadbydaylight.utils.lists.SignList;

public class Game {
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final FileConfiguration scoreboardConfig;
    private PlayerList players;
    private SignList signs;
    private GameState gameState;
    private int countdownTaskId;
    private int countdown;

    public Game(JavaPlugin plugin, FileConfiguration config, FileConfiguration scoreboardConfig) {
        this.plugin = plugin;
        this.config = config;
        this.scoreboardConfig = scoreboardConfig;
        this.players = new PlayerList(this.config, this.scoreboardConfig);
        this.signs = new SignList(plugin);
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

    public SignList getSigns() {
        return this.signs;
    }

    public void joinGame(Player player) {
        if (!player.hasPermission("deadbydaylight.play")) {
            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.noPermission"));
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
            if (this.players.getPlayersCount() == this.config.getInt("game.minPlayers")) {
                this.gameState = GameState.STARTING;
                this.startingGame();
            }
        } else if (this.gameState == GameState.STARTING) {
            this.players.addPlayer(player, PlayerType.LOBBY, this.gameState);
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
        this.playerDisconnect(player);
    }

    public void playerDisconnect(Player player) {
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

    public int getPlayersCount() {
        return this.players.getPlayersCount();
    }

    public PlayerList getPlayers() {
        return this.players;
    }

    public GameState getGameState() {
        return this.gameState;
    }
}
