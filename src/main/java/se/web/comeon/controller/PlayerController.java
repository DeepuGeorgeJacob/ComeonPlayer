package se.web.comeon.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.web.comeon.request.PlayerLoginRequest;
import se.web.comeon.request.PlayerRegisterRequest;
import se.web.comeon.request.PlayerUpdateRequest;
import se.web.comeon.response.ResponseData;
import se.web.comeon.service.PlayerService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData> register(@Valid @RequestBody final PlayerRegisterRequest player) {
        return ResponseEntity.ok(playerService.registerUser(player));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@Valid @RequestBody final PlayerLoginRequest playerLoginRequest) {
        return ResponseEntity.ok(playerService.login(playerLoginRequest));
    }

    @GetMapping("/logout/{id}")
    public ResponseEntity<ResponseData> logout(@PathVariable final long id) {
        return ResponseEntity.ok(playerService.logout(id));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseData> update(@Valid @RequestBody final PlayerUpdateRequest playerUpdateRequest) {
        return ResponseEntity.ok(playerService.update(playerUpdateRequest));
    }
}
