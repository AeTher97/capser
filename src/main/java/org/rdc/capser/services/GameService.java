package org.rdc.capser.services;

import org.rdc.capser.models.Game;
import org.rdc.capser.repositories.GamesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GamesRepository gamesRepository;

    public GameService(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    public Page<Game> getGames(Pageable pageable) {
        return gamesRepository.findAll(pageable);
    }

    public Page<Game> getPlayerGames(Pageable pageable, Long id) {
        return gamesRepository.findAllByPlayerIdOrOpponentId(pageable, id, id);
    }


}
