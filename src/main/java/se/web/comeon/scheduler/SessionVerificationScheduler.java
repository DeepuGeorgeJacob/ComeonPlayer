package se.web.comeon.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.web.comeon.entity.Player;
import se.web.comeon.repository.PlayerRepository;
import se.web.comeon.repository.SessionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class SessionVerificationScheduler {

    private static final Logger log = LogManager.getLogger(SessionVerificationScheduler.class);

    private final SessionRepository sessionRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public SessionVerificationScheduler(final SessionRepository sessionRepository, final PlayerRepository playerRepository) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
    }


    @Scheduled(fixedRate = 10000) // Every 10 seconds for demo
    private void updateSession() {
        log.info("Session verification In Progress");
        final var optionalSessions = sessionRepository.findByLogoutTimeIsNull();
        optionalSessions.ifPresent(playerSessions -> playerSessions.forEach(activeSession -> {
            final Player player = activeSession.getPlayer();
            final LocalDateTime loginTime = activeSession.getLoginTime() != null ? activeSession.getLoginTime() : getCurrentSystemTime();
            final long elapsedTime = ChronoUnit.SECONDS.between(loginTime, getCurrentSystemTime());
            final long remainingTime = player.getDailyTimeLimitSeconds() - elapsedTime;
            if (remainingTime <= 0L) {
                log.info("Logging Out User {}", player.getName());
                activeSession.setLoginTime(null);
                player.setDailyTimeLimitSeconds(0L);
                activeSession.setPlayer(player);
                activeSession.setLogoutTime(getCurrentSystemTime());
                sessionRepository.save(activeSession);
            }
        }));
        log.info("Session verification Completed");
    }

    private LocalDateTime getCurrentSystemTime() {
        return LocalDateTime.now();
    }


}
