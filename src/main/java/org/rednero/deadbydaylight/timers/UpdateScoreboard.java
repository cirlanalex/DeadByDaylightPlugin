package org.rednero.deadbydaylight.timers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.rednero.deadbydaylight.game.Game;
import org.rednero.deadbydaylight.utils.enums.GameState;
import org.rednero.deadbydaylight.utils.enums.PlayerType;

import java.util.Map;

public class UpdateScoreboard extends BukkitRunnable {
    private final Game game;
    private final FileConfiguration scoreboardConfig;
    private int counter = 0;

    public UpdateScoreboard(Game game, FileConfiguration scoreboardConfig) {
        this.game = game;
        this.scoreboardConfig = scoreboardConfig;
    }

    public void run() {
        for (Map.Entry<Player, PlayerType> entry : this.game.getPlayers().getPlayersType().entrySet()) {
            Player player = entry.getKey();
            Scoreboard scoreboard = this.game.getPlayers().getScoreboard(player);

            Objective objective = scoreboard.getObjective("scoreboard");
            objective.setDisplayName(getAnimatedValue());

            int lineId = 15;
            if (this.game.getGameState() == GameState.LOBBY) {
                for (String line : this.scoreboardConfig.getStringList("scoreboard.content.lobby")) {
                    Team team = scoreboard.getTeam("team" + lineId);
                    this.setLine(team, line.replace("%players%", String.valueOf(this.game.getPlayers().getPlayersCount())));
                    lineId--;
                }
            } else if (this.game.getGameState() == GameState.STARTING) {
                for (String line : this.scoreboardConfig.getStringList("scoreboard.content.starting")) {
                    Team team = scoreboard.getTeam("team" + lineId);
                    this.setLine(team, line.replace("%players%", String.valueOf(this.game.getPlayers().getPlayersCount())).replace("%time%", String.valueOf(this.game.getCountdown())));
                    lineId--;
                }
            } else if (this.game.getGameState() == GameState.INGAME) {
                if (this.game.getPlayers().getPlayerType(player) == PlayerType.KILLER) {
                    for (String line : this.scoreboardConfig.getStringList("scoreboard.content.inGameKiller")) {
                        Team team = scoreboard.getTeam("team" + lineId);
                        this.setLine(team, line.replace("%players%", String.valueOf(this.game.getPlayers().getPlayersCount())).replace("%survivors%", String.valueOf(this.game.getPlayers().getSurvivorsCount())));
                        lineId--;
                    }
                } else if (this.game.getPlayers().getPlayerType(player) == PlayerType.SURVIVOR) {
                    for (String line : this.scoreboardConfig.getStringList("scoreboard.content.inGameSurvivor")) {
                        Team team = scoreboard.getTeam("team" + lineId);
                        this.setLine(team, line.replace("%players%", String.valueOf(this.game.getPlayers().getPlayersCount())).replace("%survivors%", String.valueOf(this.game.getPlayers().getSurvivorsCount())));
                        lineId--;
                    }
                } else {
                    for (String line : this.scoreboardConfig.getStringList("scoreboard.content.inGameSpectator")) {
                        Team team = scoreboard.getTeam("team" + lineId);
                        this.setLine(team, line.replace("%players%", String.valueOf(this.game.getPlayers().getPlayersCount())).replace("%survivors%", String.valueOf(this.game.getPlayers().getSurvivorsCount())));
                        lineId--;
                    }
                }
            } else if (this.game.getGameState() == GameState.ENDING) {
                for (String line : this.scoreboardConfig.getStringList("scoreboard.content.ending")) {
                    Team team = scoreboard.getTeam("team" + lineId);
                    this.setLine(team, line.replace("%players%", String.valueOf(this.game.getPlayers().getPlayersCount())).replace("%time%", String.valueOf(this.game.getCountdown())));
                    lineId--;
                }
            }
        }
        this.counter++;
        if (this.counter == this.scoreboardConfig.getStringList("scoreboard.title").size()) {
            this.counter = 0;
        }
    }

    private String getAnimatedValue() {
        return this.scoreboardConfig.getStringList("scoreboard.title").get(this.counter);
    }

    private void setLine(Team team, String line) {
        if (line.length() > 16) {
            if (line.charAt(15) == '§') {
                if (line.charAt(13) == '§') {
                    team.setPrefix(line.substring(0, 13));
                    team.setSuffix(line.substring(13, line.length()));
                } else {
                    team.setPrefix(line.substring(0, 15));
                    team.setSuffix(line.substring(15, line.length()));
                }
            } else {
                if (line.charAt(14) == '§') {
                    if (line.charAt(12) == '§') {
                        team.setPrefix(line.substring(0, 12));
                        team.setSuffix(line.substring(12, line.length()));
                    } else {
                        team.setPrefix(line.substring(0, 14));
                        team.setSuffix(line.substring(14, line.length()));
                    }
                } else {
                    String lastColorCode = ChatColor.getLastColors(line.substring(0, 16));
                    team.setPrefix(line.substring(0, 16));
                    team.setSuffix(lastColorCode + line.substring(16, line.length()));
                }
            }
        } else {
            team.setPrefix(line);
        }
    }
}
