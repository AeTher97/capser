package org.rdc.capser.controllers;

import org.rdc.capser.models.Game;
import org.rdc.capser.models.Player;
import org.rdc.capser.services.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/update")
public class DatabaseUpdate {

    private DataService dataService;

    public DatabaseUpdate(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/update")
    public void update() {
        List<Player> players = dataService.getPlayersList();

        players.forEach(this::updatePlayer);
        return;
    }

    private void updatePlayer(Player player) {
        List<Game> games = dataService.getPlayerGames(player.getId());
        float averageRebottles = 0;
        int gamesWon = 0;
        int gamesLost = 0;
        int totalPointsMade = 0;
        int totalPointsLost = 0;
        int totalSinksMade = 0;
        int totalSinksLost = 0;
        int nakedLapCount = 0;

        Date date = new Date(0);

        boolean nakedLap = false;

        for (Game game : games) {
            if (game.getPlayerId().equals(player.getId())) {
                totalPointsMade += game.getPlayerScore();
                totalPointsLost += game.getOpponentScore();
                totalSinksMade += game.getPlayerSinks();
                totalSinksLost += game.getOpponentSinks();
            } else {
                totalPointsMade += game.getOpponentScore();
                totalPointsLost += game.getPlayerScore();
                totalSinksMade += game.getOpponentSinks();
                totalSinksLost += game.getPlayerSinks();
            }

            if (game.getPlayerId().equals(player.getId())) {
                averageRebottles += game.getPlayerRebottles();
            } else {
                averageRebottles += game.getOpponentRebottles();
            }
            if (game.getWinner().equals(player.getId())) {
                gamesWon++;
            } else {
                gamesLost++;
            }

            if (game.getPlayerId().equals(player.getId())) {
                if (game.getPlayerScore() == 0) {
                    nakedLap = true;
                    nakedLapCount++;
                }
            } else {
                if (game.getOpponentScore() == 0) {
                    nakedLap = true;
                    nakedLapCount++;
                }
            }

            if (game.getGameDate().compareTo(date) > 0) {
                date = game.getGameDate();
            }
        }
        if (gamesLost != 0) {
            float winLossRatio = (float) gamesWon / (float) gamesLost;
            player.setWinLossRatio(winLossRatio);

        } else {
            player.setWinLossRatio((float) gamesWon);

        }

        if (totalSinksLost != 0) {
            float sinksMadeToLost = (float) totalSinksMade / (float) totalSinksLost;
            player.setSinksMadeToLostRatio(sinksMadeToLost);

        } else {
            player.setSinksMadeToLostRatio(totalSinksMade);

        }

        averageRebottles = averageRebottles / games.size();
        if (games.size() == 0) {
            averageRebottles = 0f;
        }
        player.setAverageRebottles(averageRebottles);
        player.setGamesWon(gamesWon);
        player.setGamesLost(gamesLost);
        player.setNakedLap(nakedLap);
        player.setGamesPlayed(games.size());
        player.setTotalPointsLost(totalPointsLost);
        player.setTotalPointsMade(totalPointsMade);
        player.setTotalSinksLost(totalSinksLost);
        player.setNakedLapCount(nakedLapCount);
        player.setLastGame(date);
        player.setLastSeen(date);
        player.setTotalSinksMade(totalSinksMade);

        dataService.savePlayer(player);

    }

}
