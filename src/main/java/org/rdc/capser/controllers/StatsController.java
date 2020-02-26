package org.rdc.capser.controllers;

import org.rdc.capser.services.GameService;
import org.rdc.capser.services.PlayerService;
import org.springframework.data.domain.PageRequest;
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

    public StatsController(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @GetMapping("/games")
    public ResponseEntity<Object> getGames(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return new ResponseEntity<>(gameService.getGames(pageRequest), HttpStatus.OK);
    }

    @GetMapping("/players")
    public ResponseEntity<Object> getPlayers(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return new ResponseEntity<>(playerService.getPlayers(pageRequest), HttpStatus.OK);

    }

}
