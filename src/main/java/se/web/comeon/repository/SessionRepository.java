package se.web.comeon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.web.comeon.entity.Player;
import se.web.comeon.entity.PlayerSession;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<PlayerSession, Long> {

    Optional<PlayerSession> findByPlayer(final Player player);
    Optional<List<PlayerSession>> findByLogoutTimeIsNull();

}
