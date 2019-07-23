package org.rdc.capser.controllers;

import org.rdc.capser.Capser;
import org.rdc.capser.config.Config;
import org.rdc.capser.models.*;
import org.rdc.capser.services.DataService;
import org.rdc.capser.utilities.EloRating;
import org.rdc.capser.utilities.ErrorForm;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/")
public class GameController {

    private DataService dataService;

    public GameController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/register")
    public String addPlayer(@ModelAttribute RegisterRequest registerRequest) throws FileNotFoundException {

        PlayerList list = dataService.getPlayersList();
        List<Player> data = list.getData();

        int maxId = 0;
        for (Player player : data) {
            if (player.getId() > maxId) {
                maxId = player.getId();
            }
        }

        maxId++;
        if (registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
            dataService.addUser(registerRequest, maxId);
        } else {
            return ErrorForm.errorForm("Passwords are not matching", "register.html");
        }

        // #TODO move to config
        list.getData().add(new Player(maxId, registerRequest.getUsername(), 500));
        list.setNumberOfPlayers(list.getData().size());
        dataService.savePlayersList(list);
        Capser.restart();

        return ErrorForm.successForm("Player registered successfully");
    }

    @GetMapping("/players")
    public String getPlayers() throws FileNotFoundException {

        PlayerList list = dataService.getPlayersList();
        List<Player> data = list.getData();
        Collections.sort(data);

        StringBuilder transformedData = new StringBuilder();
        int standing = 1;

        transformedData.append("<div id=\"top_line\"><h3><pre>Standing       Id       Name                            Points \n</pre></h3></div>");
        for (Player player : data) {
            transformedData.append(String.format("<div><pre>%-3d               %-3d       %-30s        %5f \n</div>"
                    , standing, player.getId(), player.getName(), player.getPoints()));
            standing++;
        }

        return transformedData.toString();
    }

    @PostMapping(value = "/log")
    public String addGame(@ModelAttribute GameRequest gameRequest) throws FileNotFoundException {
        int playerId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        int opponentId = gameRequest.getOpponentId();
        GameType gameType = gameRequest.getGameType();
        int playerScore = gameRequest.getPlayerScore();
        int opponentScore = gameRequest.getOpponentScore();
        int playerSinks = gameRequest.getPlayerSinks();
        int opponentSinks = gameRequest.getOpponentSinks();

        if (playerId > opponentId) {
            int tempId = playerId;
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

        GamesList list = dataService.getGamesList();
        List<Game> data = list.getData();
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
        int winner;

        boolean d;
        if (playerScore > opponentScore) {
            d = true;
            winner = playerId;
        } else {
            d = false;
            winner = opponentId;
        }

        Game game = new Game(playerId, opponentId, gameType, playerScore, opponentScore, playerSinks, opponentSinks, winner);
        list.getData().add(game);
        list.setNumberOfGames(list.getData().size());
        dataService.saveGamesList(list);

        Player player1 = dataService.findPlayerById(playerId);
        Player player2 = dataService.findPlayerById(opponentId);
        List<Player> listToPass = new ArrayList<>();
        listToPass.add(player1);
        listToPass.add(player2);

        List<Player> updatedList = EloRating.calculate(listToPass, 30, d);

        PlayerList listToModify = dataService.getPlayersList();
        List<Player> playersToModify = listToModify.getData();
        for (Player player : playersToModify) {
            if (player.getId() == updatedList.get(0).getId()) {
                player.setPoints(updatedList.get(0).getPoints());
                updatePlayer(player);
            } else if (player.getId() == updatedList.get(1).getId()) {
                player.setPoints(updatedList.get(1).getPoints());
                updatePlayer(player);
            }
        }
        dataService.savePlayersList(listToModify);
        return ErrorForm.successForm("Game saved successfully");

    }

    private void updatePlayer(Player player) {
        List<Game> games = dataService.getPlayerGames(player.getId());
        float averageRebottles = 0;
        int gamesWon = 0;
        int gamesLost = 0;
        for (Game game : games) {
            if (game.getPlayerId() == player.getId()) {
                averageRebottles += game.getPlayerRebottles();
            } else {
                averageRebottles += game.getOpponentRebottles();
            }
            if (game.getWinner() == player.getId()) {
                gamesWon++;
            } else {
                gamesLost++;
            }
        }
        float winLossRatio = gamesWon / gamesLost;

        averageRebottles = averageRebottles / games.size();
        player.setAverageRebottles(averageRebottles);
        player.setGamesWon(gamesWon);
        player.setWinLossRatio(winLossRatio);
        player.setGamesLost(gamesLost);
        player.setGamesPlayed(games.size());
    }


    @GetMapping("/games")
    public String getGames() throws FileNotFoundException {

        GamesList list = dataService.getGamesList();
        List<Game> data = list.getData();

        StringBuilder transformedData = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        transformedData.append("<div id=\"games_top\"><pre>Players                                Score        Sinks      Rebuttals     Winner          Game Type        Game Time\n </div><pre>");

        for (Game game : data) {
            String playerName = dataService.getPlayerName(game.getPlayerId());
            String opponent = dataService.getPlayerName(game.getOpponentId());
            transformedData.append(String.format("<div><pre>%-15s vs %15s     %-2d : %2d      %-2d : %2d    %-2d : %2d       %-15s %-12s   %30s \n" + "</pre></div>",
                    dataService.getPlayerName(game.getPlayerId()),
                    dataService.getPlayerName(game.getOpponentId()),
                    game.getPlayerScore(),
                    game.getOpponentScore(),
                    game.getPlayerSinks(),
                    game.getOpponentSinks(),
                    game.getPlayerRebottles(),
                    game.getOpponentRebottles(),
                    dataService.getPlayerName(game.getWinner()),
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
    public String getPlayerStats(@RequestParam(required = false, name = "id") String id) {
        try {
            Player player = dataService.findPlayerById(
                    Integer.parseInt(StringUtils.isEmpty(id) ?
                            SecurityContextHolder.getContext().getAuthentication().getName() : id));

            StringBuilder transformedData = new StringBuilder();
            transformedData.append("<div><pre><h3>Player Statistics</h3></div>");
            transformedData.append("<div><pre>Name                                " + player.getName() + "</div>");
            transformedData.append("<div><pre>Id                                  " + player.getId() + "</div>");
            transformedData.append("<div><pre>Points                              " + player.getPoints() + "</div>");
            transformedData.append("<div><pre>Games played                        " + player.getGamesPlayed() + "</div>");
            transformedData.append("<div><pre>Games won                           " + player.getGamesWon() + "</div>");
            transformedData.append("<div><pre>Games lost                          " + player.getGamesLost() + "</div>");
            transformedData.append("<div><pre>Win/Loss Ratio                      " + player.getWinLossRatio() + "</div>");
            transformedData.append("<div><pre><Average Rebuttals:                 " + player.getAverageRebottles() + "</div>");

            return transformedData.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
