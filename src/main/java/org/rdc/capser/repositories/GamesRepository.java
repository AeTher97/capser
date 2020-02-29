package org.rdc.capser.repositories;

import org.rdc.capser.models.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamesRepository extends JpaRepository<Game, Long> {
    Page<Game> findAllByPlayerIdOrOpponentId(Pageable pageable, Long playerId, Long opponentId);

    List<Game> findAllByPlayerIdOrOpponentId(Long playerId, Long opponentId);

    List<Game> findAllByOpponentId(Long id);

}
