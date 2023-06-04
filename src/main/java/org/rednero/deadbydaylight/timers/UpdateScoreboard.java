package org.rednero.deadbydaylight.timers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.rednero.deadbydaylight.game.Game;
import org.rednero.deadbydaylight.utils.enums.GameState;
import org.rednero.deadbydaylight.utils.enums.PlayerType;

import java.util.Map;

public class UpdateScoreboard extends BukkitRunnable {
    private final Game game;
    private final ScoreboardManager scoreboardManager;
    private int counter = 0;
    public UpdateScoreboard(Game game) {
        this.game = game;
        this.scoreboardManager = Bukkit.getScoreboardManager();
    }

    public void run() {
        for (Map.Entry<Player, PlayerType> entry : this.game.getPlayers().getPlayersType().entrySet()) {
            Scoreboard scoreboard = this.scoreboardManager.getNewScoreboard();

            Objective objective = scoreboard.registerNewObjective("test", "test");
            objective.setDisplayName(getAnimatedValue());
            objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);

            Player player = entry.getKey();
            objective.getScore("§7Online: §f" + this.game.getPlayersCount()).setScore(1);
            if (this.game.getGameState() == GameState.LOBBY) {
                objective.getScore("§7LOBBY").setScore(2);
            } else if (this.game.getGameState() == GameState.STARTING) {
                objective.getScore("§7STARTING in " + this.game.getCountdown() + "s").setScore(2);
            } else if (this.game.getGameState() == GameState.INGAME) {
                objective.getScore("§7INGAME").setScore(2);
                if (this.game.getPlayers().getPlayerType(player) == PlayerType.KILLER) {
                    objective.getScore("§7Role: §cKiller").setScore(3);
                } else if (this.game.getPlayers().getPlayerType(player) == PlayerType.SURVIVOR) {
                    objective.getScore("§7Role: §aSurvivor").setScore(3);
                } else {
                    objective.getScore("§7Role: §7Spectator").setScore(3);
                }
            } else if (this.game.getGameState() == GameState.ENDING) {
                objective.getScore("§7ENDING in " + this.game.getCountdown() + "s").setScore(2);
            }
            player.setScoreboard(scoreboard);
        }
        this.counter++;
        if (this.counter == 2) {
            this.counter = 0;
        }
    }

    private String getAnimatedValue() {
        switch (this.counter) {
            case 0:
                return "§c§lDBD";
            case 1:
                return "§f§lDBD";
        }
        return "";
    }
}
