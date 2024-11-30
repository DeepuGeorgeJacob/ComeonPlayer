package se.web.comeon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import se.web.comeon.controller.PlayerController;
import se.web.comeon.request.PlayerLoginRequest;
import se.web.comeon.request.PlayerRegisterRequest;
import se.web.comeon.request.PlayerUpdateRequest;
import se.web.comeon.response.ResponseData;
import se.web.comeon.service.PlayerService;

import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    private PlayerRegisterRequest registerRequest;
    private PlayerLoginRequest loginRequest;
    private PlayerUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerRequest = new PlayerRegisterRequest("John", "Doe", "john.doe@example.com", "password123", "1990-01-01", "123 Street");
        loginRequest = new PlayerLoginRequest("john.doe@example.com", "password123");
        updateRequest = new PlayerUpdateRequest(1L, true, 30);
    }

    @Test
    void testRegister() {
        // GIVEN
        ResponseData responseData = ResponseData.builder().info("Player John registered with Id 1").build();
        when(playerService.registerUser(registerRequest)).thenReturn(responseData);

        // WHEN
        ResponseEntity<ResponseData> response = playerController.register(registerRequest);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(Objects.requireNonNull(response.getBody())).getInfo()).isEqualTo("Player John registered with Id 1");
        verify(playerService, times(1)).registerUser(registerRequest);
    }

    @Test
    void testLogin() {
        // GIVEN
        ResponseData responseData = ResponseData.builder().info("User logged In with session Id 1 With remaining 3600 Seconds").build();
        when(playerService.login(loginRequest)).thenReturn(responseData);

        // WHEN
        ResponseEntity<ResponseData> response = playerController.login(loginRequest);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getInfo()).isEqualTo("User logged In with session Id 1 With remaining 3600 Seconds");
        verify(playerService, times(1)).login(loginRequest);
    }

    @Test
    void testUpdate() {
        // GIVEN
        ResponseData responseData = ResponseData.builder().info("Player John is Updated").build();
        when(playerService.update(updateRequest)).thenReturn(responseData);

        // WHEN
        ResponseEntity<ResponseData> response = playerController.update(updateRequest);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getInfo()).isEqualTo("Player John is Updated");
        verify(playerService, times(1)).update(updateRequest);
    }

    @Test
    void testLogout() {
        // GIVEN
        ResponseData responseData = ResponseData.builder().data("Player logged out successfully").build();
        when(playerService.logout(1L)).thenReturn(responseData);

        // WHEN
        ResponseEntity<ResponseData> response = playerController.logout(1L);

        // THEN
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo("Player logged out successfully");
        verify(playerService, times(1)).logout(1L);
    }
}
