package se.web.comeon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.web.comeon.entity.PlayerSession;
import se.web.comeon.repository.PlayerRepository;
import se.web.comeon.entity.Player;
import se.web.comeon.error.UserHandleException;
import se.web.comeon.repository.SessionRepository;
import se.web.comeon.request.PlayerLoginRequest;
import se.web.comeon.request.PlayerRegisterRequest;
import se.web.comeon.request.PlayerUpdateRequest;
import se.web.comeon.response.LogOutResponse;
import se.web.comeon.response.ResponseData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PlayerService {

    private final PasswordEncoder passwordEncoder;
    private final PlayerRepository playerRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public PlayerService(
            final PasswordEncoder passwordEncoder,
            final PlayerRepository playerRepository,
            final SessionRepository sessionRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
    }

    public ResponseData registerUser(final PlayerRegisterRequest playerRegisterRequest) {
        var activePlayer = playerRepository.findByEmail(playerRegisterRequest.getEmail());
        if (activePlayer.isPresent()) {
            throw new UserHandleException(activePlayer.get().getName() + " already registered.", HttpStatus.CONFLICT);
        }
        final Player player = new Player();
        player.setName(playerRegisterRequest.getName());
        player.setSurname(playerRegisterRequest.getSurname());
        player.setPassword(passwordEncoder.encode(playerRegisterRequest.getPassword()));
        player.setEmail(playerRegisterRequest.getEmail());
        player.setDateOfBirth(LocalDate.parse(playerRegisterRequest.getDateOfBirth()));
        player.setAddress(playerRegisterRequest.getAddress());
        player.setActive(true);
        var registeredUser = playerRepository.save(player);
        return ResponseData.builder().info(String.format("Player %1s registered with Id %2s", playerRegisterRequest.getName(), registeredUser.getId())).build();

    }

    @Transactional(noRollbackFor = UserHandleException.class)
    public ResponseData login(final PlayerLoginRequest playerLoginRequest) {
        final Player player = playerRepository.findByEmail(playerLoginRequest.getEmail()).orElseThrow(() -> new UserHandleException("Player not found. Try to register", HttpStatus.NOT_FOUND));
        if (!player.isActive()) {
            throw new UserHandleException("Player not active", HttpStatus.FORBIDDEN);
        }
        if (passwordEncoder.matches(playerLoginRequest.getPassword(), player.getPassword())) {
            return createOrUpdateLoginSession(player);
        } else {
            throw new UserHandleException("Invalid Password", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public ResponseData update(final PlayerUpdateRequest playerUpdateRequest) {
        final Player player = playerRepository.findById(playerUpdateRequest.getPlayerId()).orElseThrow(() -> new UserHandleException("Player not found. Try to register", HttpStatus.NOT_FOUND));
        var isActive = playerUpdateRequest.getIsActive();
        if (isActive != null) {
            player.setActive(isActive);
        }
        var timeLimitInMinutes = playerUpdateRequest.getTimeLimitInMinutes();
        if (timeLimitInMinutes != null) {
            var timeLimitInSeconds = timeLimitInMinutes * 60L;
            player.setDailyTimeLimitSeconds(player.getDailyTimeLimitSeconds() + timeLimitInSeconds);
        }
        var updatedPlayer = playerRepository.save(player);
        return ResponseData.builder().info("Player " + updatedPlayer.getName() + " is Updated").build();
    }


    public ResponseData logout(final long sessionId) {
        var playerSession = sessionRepository.findById(sessionId).orElseThrow(() -> new UserHandleException("Please login before logout", HttpStatus.NOT_FOUND));
        if (playerSession.getLogoutTime() != null) {
            throw new UserHandleException("User already logged out. Logout first then login", HttpStatus.FORBIDDEN);
        }
        final Player player = playerSession.getPlayer();
        final LocalDateTime currentTime = getCurrentSystemTime();
        final long elapsedTime = ChronoUnit.SECONDS.between(playerSession.getLoginTime(), currentTime);
        final long remainingTime = player.getDailyTimeLimitSeconds() - elapsedTime;
        player.setDailyTimeLimitSeconds(Math.max(remainingTime, 0L));
        playerSession.setLoginTime(null);
        playerSession.setLogoutTime(currentTime);
        var savedSession = sessionRepository.save(playerSession);
        return ResponseData.builder().data(new LogOutResponse(savedSession.getLoginTime(), savedSession.getLogoutTime(), savedSession.getPlayer().getDailyTimeLimitSeconds())).build();

    }

    private ResponseData createOrUpdateLoginSession(final Player player) {
        var remainingDailyLimit = player.getDailyTimeLimitSeconds();
        if (remainingDailyLimit <= 0L) {
            throw new UserHandleException("You don't enough time to play this game. Please update your daily limit first before login.", HttpStatus.FORBIDDEN);
        }
        var optionalPlayerSession = sessionRepository.findByPlayer(player);
        var currentDateTime = getCurrentSystemTime();
        if (optionalPlayerSession.isPresent()) {
            final PlayerSession activeSession = optionalPlayerSession.get();
            final LocalDateTime loginTime = activeSession.getLoginTime() != null ? activeSession.getLoginTime() : getCurrentSystemTime();
            final long elapsedTime = ChronoUnit.SECONDS.between(loginTime, getCurrentSystemTime());
            final long remainingTime = remainingDailyLimit - elapsedTime;
            return checkAndUpdateUserSession(activeSession, remainingTime);
        } else {
            final var session = new PlayerSession();
            session.setLoginTime(currentDateTime);
            session.setPlayer(player);
            sessionRepository.save(session);
            return ResponseData.builder().info("User logged In with session Id " + session.getId() + " With remaining " + remainingDailyLimit + " Seconds").build();

        }
    }

    private ResponseData checkAndUpdateUserSession(final PlayerSession playerSession, final long remainingTime) {
        if (playerSession.getLogoutTime() == null) { // Used logged in but not logged Out
            if (remainingTime < 0L) { // Daily limit reached
               sessionUpdateForLogOut(playerSession);
                throw new UserHandleException("You have reached your daily limit, Logging Out", HttpStatus.FORBIDDEN);
            } else {
                throw new UserHandleException("You have already loggedIn with remaining " + remainingTime + " Seconds", HttpStatus.FORBIDDEN);
            }
        } else {
            if (remainingTime < 0) {
                sessionUpdateForLogOut(playerSession);
                throw new UserHandleException("Can't able to login, You have reached your daily limit", HttpStatus.FORBIDDEN);
            }
        }
        if (playerSession.getLoginTime() == null) {
            playerSession.setLoginTime(getCurrentSystemTime());
        }
        playerSession.setLogoutTime(null);
        sessionRepository.save(playerSession);
        return ResponseData.builder().info("You have loggedIn with remaining " + remainingTime + " Seconds").build();
    }

    private void sessionUpdateForLogOut(final PlayerSession playerSession) {
        final Player player = playerSession.getPlayer();
        player.setDailyTimeLimitSeconds(0L);
        playerSession.setLogoutTime(getCurrentSystemTime());
        playerSession.setLoginTime(null);
        playerSession.setPlayer(player);
        sessionRepository.save(playerSession);
    }


    private LocalDateTime getCurrentSystemTime() {
        return LocalDateTime.now();
    }


}
