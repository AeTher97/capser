package org.rdc.capser.controllers;

import org.rdc.capser.config.Config;
import org.rdc.capser.models.*;
import org.rdc.capser.services.DataService;
import org.rdc.capser.utilities.EloRating;
import org.rdc.capser.utilities.ErrorForm;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class GameController {

    private DataService dataService;

    public GameController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/register")
    public String addPlayer(@RequestBody RegisterRequest registerRequest) throws FileNotFoundException {


        if (registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
            dataService.addUser(registerRequest);
            return ErrorForm.successForm("Player registered successfully");
        } else {
            return ErrorForm.errorForm("Passwords are not matching", "register.html");
        }

        // #TODO move to config

    }

    @GetMapping("/players")
    public String getPlayers() throws FileNotFoundException {

        List<Player> list = dataService.getPlayersList();

        Collections.sort(list);
        list = list.stream().filter(i -> i.getGamesPlayed() + 1 > Config.getPlacementMatchesNumber()).collect(Collectors.toList());


        StringBuilder transformedData = new StringBuilder();
        int standing = 1;

        transformedData.append("<div id=\"top_line\"><h3><pre>Standing       Id       Name                            Points \n</pre></h3></div>");
        for (Player player : list) {
            transformedData.append(String.format("<div><pre>%-3d               %-3d       %-30s        %5f \n</div>"
                    , standing, player.getId(), player.getName(), player.getPoints()).replace(",", "."));
            standing++;
        }

        return transformedData.toString();
    }

    @PostMapping(value = "/log")
    public String addGame(@RequestBody GameRequest gameRequest) throws FileNotFoundException {
        Long playerId = dataService.findPlayerByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        Long opponentId = (long) gameRequest.getOpponentId();
        GameType gameType = gameRequest.getGameType();
        int playerScore = gameRequest.getPlayerScore();
        int opponentScore = gameRequest.getOpponentScore();
        int playerSinks = gameRequest.getPlayerSinks();
        int opponentSinks = gameRequest.getOpponentSinks();


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
            return ErrorForm.errorForm("Game cannot end in a draw", "/gamePost.html");
        }

        if (opponentScore < 11 && playerScore < 11) {
            return ErrorForm.errorForm("Game must end with one of the players obtaining 11 points", "/gamePost.html");
        }

        List<Game> list = dataService.getGamesList();
        if (gameType == null) {
            if (opponentScore > 11 || playerScore > 11) {
                gameType = GameType.OVERTIME;
            } else {
                gameType = GameType.SUDDEN_DEATH;
            }
        }

        if (gameType == GameType.OVERTIME && Math.abs(opponentScore - playerScore) != 2) {
            return ErrorForm.errorForm("Overtime game must finish with 2 points advantage", "/gamePost.html");
        } else if (gameType == GameType.SUDDEN_DEATH && (opponentScore != 11 && playerScore != 11)) {
            return ErrorForm.errorForm("Sudden death game must finish with 11 points", "/gamePost.html");
        }

        if (opponentId == playerId) {
            return ErrorForm.errorForm("Cannot post game against yourself", "/gamePost.html");
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

        Game game = new Game(playerId, opponentId, gameType, playerScore, opponentScore, playerSinks, opponentSinks, winner);
        dataService.saveGame(game);

        Player player1 = dataService.findPlayerById(playerId);
        Player player2 = dataService.findPlayerById(opponentId);
        List<Player> listToPass = new ArrayList<>();
        listToPass.add(player1);
        listToPass.add(player2);

        List<Player> updatedList = EloRating.calculate(listToPass, 30, d);

        assert updatedList != null;
        for (Player player : updatedList) {
            updatePlayer(player);
            dataService.savePlayer(player);
        }
        return ErrorForm.successForm("Game saved successfully");

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
        }
        if (gamesLost != 0) {
            float winLossRatio = new Float(gamesWon) / new Float(gamesLost);
            player.setWinLossRatio(winLossRatio);

        } else {
            float winLossRatio = gamesWon;
            player.setWinLossRatio(winLossRatio);

        }

        if (totalSinksLost != 0) {
            float sinksMadeToLost = new Float(totalSinksMade) / new Float(totalSinksLost);
            player.setSinksMadeToLostRatio(sinksMadeToLost);

        } else {
            player.setSinksMadeToLostRatio(totalSinksMade);

        }

        averageRebottles = averageRebottles / games.size();
        player.setAverageRebottles(averageRebottles);
        player.setGamesWon(gamesWon);
        player.setGamesLost(gamesLost);
        player.setGamesPlayed(games.size());
        player.setTotalPointsLost(totalPointsLost);
        player.setTotalPointsMade(totalPointsMade);
        player.setTotalSinksLost(totalSinksLost);
        player.setTotalSinksMade(totalSinksMade);

    }


    @GetMapping("/games")
    public String getGames(@RequestParam(required = false) Long id, @RequestParam(required = false) SortType sortBy) throws FileNotFoundException {

        List<Game> list = dataService.getGamesList();

        StringBuilder transformedData = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        transformedData.append("<div id=\"games_top\"><pre>Players                                Score        Sinks      Rebuttals     Winner          Game Type        Game Time\n </div><pre>");
        Collections.sort(list);

        for (Game game : list) {
            if (id != null) {
                if (game.getPlayerId() != id && game.getOpponentId() != id) {
                    continue;
                }
            }
            String playerName = dataService.findPlayerById(game.getPlayerId()).getName();
            String opponentName = dataService.findPlayerById(game.getOpponentId()).getName();
            String winnerName = dataService.findPlayerById(game.getWinner()).getName();
            transformedData.append(String.format("<div><pre>%-15s vs %15s     %-2d : %2d      %-2d : %2d    %-2d : %2d       %-15s %-12s   %30s \n" + "</pre></div>",
                    playerName,
                    opponentName,
                    game.getPlayerScore(),
                    game.getOpponentScore(),
                    game.getPlayerSinks(),
                    game.getOpponentSinks(),
                    game.getPlayerRebottles(),
                    game.getOpponentRebottles(),
                    winnerName,
                    game.getGameType(),
                    game.getGameDate()));
        }

        return transformedData.toString();
    }

    @GetMapping("/version")
    public String getVersion() {
        return "<div class=\"version_left\">   Capser build " + Config.getBuildNumber() + "</div><div class=\"version_right\">Made with love by Mike   </div>";
    }

    @GetMapping("/stats")
    public String getPlayerStats(@RequestParam(required = false, name = "id") Long id) {

        Player player = dataService.findPlayerById(
                id == null ?
                        dataService.findPlayerByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId() : id);


        StringBuilder transformedData = new StringBuilder();
        transformedData.append("<div id=\"statistics_top\"><pre><h3>Player Statistics</h3></div>");
        transformedData.append("<div><pre>Name                                " + player.getName() + "</div>");
        transformedData.append("<div><pre>Id                                  " + player.getId() + "</div>");
        transformedData.append("<div><pre>Points                              " + player.getPoints() + "</div>");
        transformedData.append("<div><pre>Games played                        " + player.getGamesPlayed() + "</div>");
        transformedData.append("<div><pre>Games won                           " + player.getGamesWon() + "</div>");
        transformedData.append("<div><pre>Games lost                          " + player.getGamesLost() + "</div>");
        transformedData.append("<div><pre>Win/Loss Ratio                      " + player.getWinLossRatio() + "</div>");
        transformedData.append("<div><pre>Average Rebuttals:                  " + player.getAverageRebottles() + "</div>");
        transformedData.append("<div><pre>Total points made:                  " + player.getTotalPointsMade() + "</div>");
        transformedData.append("<div><pre>Total points lost:                  " + player.getTotalPointsLost() + "</div>");
        transformedData.append("<div><pre>Total sinks made:                   " + player.getTotalSinksMade() + "</div>");
        transformedData.append("<div><pre>Total sinks lost:                   " + player.getTotalSinksLost() + "</div>");
        transformedData.append("<div><pre>Sinks made to lost ratio:           " + player.getSinksMadeToLostRatio() + "</div>");


        return transformedData.toString();

    }

    @GetMapping("/playerIdsAndNames")
    public String getPlayerIdsAndNames(@RequestParam(required = false) String type) {
        try {
            if (type != null && type.equals("id")) {
                List<Player> playerList = dataService.getPlayersList();
                StringBuilder data = new StringBuilder();
                data.append("<select name=\"id\">");
                data.append("<option value=\"0\">Select</option>");
                playerList.stream()
                        .forEach(p -> data.append("<option value=\"" + p.getId() + "\">" + p.getName() + "</option>"));
                data.append("</select>");

                return data.toString();
            }
            List<Player> playerList = dataService.getPlayersList();
            StringBuilder data = new StringBuilder();
            data.append("<select name=\"opponentId\">");
            data.append("<option value=\"0\">Select</option>");

            playerList.stream()
                    .forEach(p -> data.append("<option value=\"" + p.getId() + "\">" + p.getName() + "</option>"));
            data.append("</select>");

            return data.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Error retrieving players";
        }
    }
}
