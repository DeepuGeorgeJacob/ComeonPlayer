package se.web.comeon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.web.comeon.entity.Player;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByEmail(String email);
}
