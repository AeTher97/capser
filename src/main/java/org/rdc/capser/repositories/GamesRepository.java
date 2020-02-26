package org.rdc.capser.repositories;

import org.rdc.capser.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamesRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByPlayerId(Long id);

    List<Game> findAllByPlayerIdOrOpponentId(Long playerId, Long opponentId);

    List<Game> findAllByOpponentId(Long id);

}
