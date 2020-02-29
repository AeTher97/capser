package org.rdc.capser.services;

import org.rdc.capser.models.Player;
import org.rdc.capser.repositories.PlayersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayersRepository playersRepository;

    public PlayerService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public Page<Player> getPlayers(Pageable pageable) {
        return playersRepository.findAll(pageable);
    }

    public List<Player> getAllPlayers() {
        return playersRepository.findAll();
    }

    public String getPlayerName(Long id) {
        return playersRepository.findPlayerById(id).getName();
    }

    public Long getIdFromName(String name) {
        return playersRepository.findPlayerByName(name).getId();
    }

    public Player getPlayerById(Long id) {
        return playersRepository.findPlayerById(id);
    }

    public void savePlayer(Player player) {
        playersRepository.save(player);
    }

}
