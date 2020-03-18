package org.rdc.capser.controllers;

import org.rdc.capser.models.*;
import org.rdc.capser.services.DataService;
import org.rdc.capser.utilities.EloRating;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityExistsException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("action/")
public class GameController {

    private DataService dataService;

    public GameController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> addPlayer(@RequestBody RegisterRequest registerRequest) throws FileNotFoundException {


        try {
            if (registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
                dataService.addUser(registerRequest);
                return new ResponseEntity<>(new Status("Registered successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Status("Error", "Passwords not matching"), HttpStatus.BAD_REQUEST);
            }
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(new Status("Error", "This username is taken"), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/log")
    public ResponseEntity<Object> addGame(@RequestBody GameRequest gameRequest) throws FileNotFoundException {
        Long playerId = dataService.findPlayerByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        Long opponentId = (long) gameRequest.getOpponentId();
        GameType gameType = gameRequest.getGameType();
        int playerScore = gameRequest.getPlayerScore();
        int opponentScore = gameRequest.getOpponentScore();
        int playerSinks = gameRequest.getPlayerSinks();
        int opponentSinks = gameRequest.getOpponentSinks();

        Player player1 = dataService.findPlayerById(playerId);
        Player player2 = dataService.findPlayerById(opponentId);
        if (player1 == null || player2 == null) {
            return new ResponseEntity<>(new Status("Error", "This user doesn't exist"), HttpStatus.NOT_FOUND);
        }


        if (playerId > opponentId) {
            Long tempId = playerId;
            playerId = opponentId;
            opponentId = tempId;

            int tempScore = playerScore;
            playerScore = opponentScore;
            opponentScore = tempScore;

            int tempSinks = playerSinks;
            playerSinks = opponentSinks;
            opponentSinks = tempSinks;
        }

        if (opponentScore == playerScore) {
            return new ResponseEntity<>(new Status("Error", "Game cannot end in a draw"), HttpStatus.BAD_REQUEST);
        }

        if (opponentScore < 11 && playerScore < 11) {
            return new ResponseEntity<>(new Status("Error", "Game must end with one of the players obtaining 11 points"), HttpStatus.BAD_REQUEST);
        }

        if (gameType == null) {
            if (opponentScore > 11 || playerScore > 11) {
                gameType = GameType.OVERTIME;
            } else {
                gameType = GameType.SUDDEN_DEATH;
            }
        }

        if (gameType == GameType.OVERTIME && Math.abs(opponentScore - playerScore) != 2) {
            return new ResponseEntity<>(new Status("Error", "Overtime game must finish with 2 points advantage"), HttpStatus.BAD_REQUEST);
        } else if (gameType == GameType.SUDDEN_DEATH && (opponentScore != 11 && playerScore != 11)) {
            return new ResponseEntity<>(new Status("Error", "Sudden death game must finish with 11 points"), HttpStatus.BAD_REQUEST);
        }

        if (opponentId.equals(playerId)) {
            return new ResponseEntity<>(new Status("Error", "Cannot post game against yourself"), HttpStatus.BAD_REQUEST);
        }
        Long winner;

        boolean d;
        if (playerScore > opponentScore) {
            d = true;
            winner = playerId;
        } else {
            d = false;
            winner = opponentId;
        }



        List<Player> listToPass = new ArrayList<>();
        listToPass.add(player1);
        listToPass.add(player2);

        float playerPreviousRating = player1.getPoints();
        float opponentPreviousRating = player2.getPoints();

        List<Player> updatedList = EloRating.calculate(listToPass, 30, d);


        Game game = new Game(playerId, opponentId, gameType, playerScore, opponentScore, playerSinks, opponentSinks, winner, 0f, 0f);
        assert updatedList != null;
        for (Player player : updatedList) {
            updatePlayer(player);
            dataService.savePlayer(player);
            if (player.getId().equals(playerId)) {
                game.setPlayerPointsChange(player.getPoints() - playerPreviousRating);
            }
            if (player.getId().equals(opponentId)) {
                game.setOpponentPointsChange(player.getPoints() - opponentPreviousRating);
            }
        }


        dataService.saveGame(game);
        return new ResponseEntity<>(new Status("Game saved successfully"), HttpStatus.CREATED);

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
        player.setLastGame(new Date(System.currentTimeMillis()));
        player.setTotalSinksMade(totalSinksMade);

    }


}
