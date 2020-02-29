package org.rdc.capser.repositories;

import org.rdc.capser.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayersRepository extends JpaRepository<Player, Long> {
    Player findPlayerById(Long id);

    Player findPlayerByName(String username);
}
