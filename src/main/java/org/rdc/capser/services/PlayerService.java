package org.rdc.capser.services;

import org.rdc.capser.models.Player;
import org.rdc.capser.repositories.PlayersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayersRepository playersRepository;

    public PlayerService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public Page<Player> getPlayers(Pageable pageable) {
        return playersRepository.findAll(pageable);
    }
}
