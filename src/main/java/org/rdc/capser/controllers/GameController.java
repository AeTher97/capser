package org.rdc.capser.controllers;

import org.rdc.capser.config.Config;
import org.rdc.capser.models.*;
import org.rdc.capser.services.DataService;
import org.rdc.capser.utilities.EloRating;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/caps")
public class GameController {

    private DataService dataService;

    public GameController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/add/player")
    public void addPlayer(@RequestParam("name") String name) throws FileNotFoundException {

        PlayerList list = dataService.getPlayersList();
        List<Player> data = list.getData();

        int maxId = 0;
        for (Player player : data) {
            if (player.getId() > maxId) {
                maxId = player.getId();
            }
        }

        maxId++;

        // #TODO move to config
        list.getData().add(new Player(maxId, name, 500));
        list.setNumberOfPlayers(list.getData().size());
        dataService.savePlayersList(list);
    }

    @GetMapping("/get/players")
    public String getPlayers() throws FileNotFoundException {

        PlayerList list = dataService.getPlayersList();
        List<Player> data = list.getData();
        Collections.sort(data);

        StringBuilder transformedData = new StringBuilder();
        int standing = 1;

        transformedData.append("<pre>Standing       Id       Name                            Points \n<br>");
        for (Player player : data) {
            transformedData.append(String.format("<pre>%3d          %3d        %-30s  %5f \n<br>"
                    , standing, player.getId(), player.getName(), player.getPoints()));
            standing++;
        }

        transformedData.append(String.format("<br><br><br>Capser© build %.2f           Made with love by Mike", Config.getBuildNumber()));

        return transformedData.toString();
    }

    @PostMapping("/add/game")
    public String addGame(@RequestBody GameRequest gameRequest) throws FileNotFoundException {
        int playerId = gameRequest.getPlayerId();
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
            return "Game cannot end with a draw.";
        }

        if (opponentScore < 11 && playerScore < 11) {
            return "Game cannot end with less than 11 points";
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
            return "Overtime game must finish with 2 points advantage.";
        } else if (gameType == GameType.SUDDEN_DEATH && (opponentScore != 11 && playerScore != 11)) {
            return "Sudden death game must finish with 11 points.";
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
            } else if (player.getId() == updatedList.get(1).getId()) {
                player.setPoints(updatedList.get(1).getPoints());
            }
        }
        dataService.savePlayersList(listToModify);
        return "Game saved successfully";

    }

    @GetMapping("/get/games")
    public String getGames() throws FileNotFoundException {

        GamesList list = dataService.getGamesList();
        List<Game> data = list.getData();

        StringBuilder transformedData = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        transformedData.append("<pre>Players                                Score        Sinks      Rebottles     Winner          Game Type        Game Time\n <br>");

        for (Game game : data) {
            String playerName = dataService.getPlayerName(game.getPlayerId());
            String opponent = dataService.getPlayerName(game.getOpponentId());
            transformedData.append(String.format("%-15s vs %15s     %-2d : %2d      %-2d : %2d    %-2d : %2d       %-15s %-12s   %30s \n" + "<br>",
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

        transformedData.append(String.format("<br><br><br>Capser© build %.2f           Made with love by Mike", Config.getBuildNumber()));
        return transformedData.toString();
    }

}
