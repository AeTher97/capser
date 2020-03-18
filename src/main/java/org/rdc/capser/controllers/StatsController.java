package org.rdc.capser.controllers;

import org.rdc.capser.models.Game;
import org.rdc.capser.models.response.GameResponse;
import org.rdc.capser.services.DataService;
import org.rdc.capser.services.GameService;
import org.rdc.capser.services.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final GameService gameService;
    private final PlayerService playerService;
    private final DataService dataService;

    public StatsController(GameService gameService, PlayerService playerService, DataService dataService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.dataService = dataService;
    }

    @GetMapping("/games")
    public ResponseEntity<Object> getGames(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, @RequestParam(required = false) Long id) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("gameDate").descending().and(Sort.by("id").and(Sort.by("id"))));

        Page<Game> games = null;

        if (id == null) {
            games = gameService.getGames(pageRequest);
        } else {
            games = gameService.getPlayerGames(pageRequest, id);

        }

        if (games.getSize() == 0) {
            return new ResponseEntity<>(games, HttpStatus.OK);
        }

        Page<GameResponse> gamesResponse = games.map(game -> {
            String playerName = playerService.getPlayerName(game.getPlayerId());
            String opponentName = playerService.getPlayerName(game.getOpponentId());
            GameResponse gameResponse = new GameResponse(game);
            gameResponse.setOpponentName(opponentName);
            gameResponse.setPlayerName(playerName);
            if (game.getWinner().

                    equals(game.getPlayerId())) {
                gameResponse.setWinner(playerName);
            } else {
                gameResponse.setWinner(opponentName);
            }

            return gameResponse;
        });

        return new ResponseEntity<>(gamesResponse, HttpStatus.OK);
    }

    @GetMapping("/players")
    public ResponseEntity<Object> getPlayers(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("points").descending().and(Sort.by("name").and(Sort.by("id"))));
        return new ResponseEntity<>(playerService.getPlayers(pageRequest), HttpStatus.OK);
    }

    @GetMapping("/all/players")
    public ResponseEntity<Object> getAllPlayers() {

        return new ResponseEntity<>(playerService.getAllPlayers(), HttpStatus.OK);
    }

    @GetMapping("/player")
    public ResponseEntity<Object> getPlayer(@RequestParam Long id) {
        return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.OK);
    }

    @GetMapping("/version")
    public String getVersion() {
        return "{\"version\":" + +dataService.getCapserVersion() + "}";
    }


}
