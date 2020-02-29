package org.rdc.capser.repositories;

import org.rdc.capser.models.Creds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Creds, Long> {
    Creds findCredsById(Long id);

    Creds findCredsByUsername(String username);
}
