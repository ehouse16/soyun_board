package board.soyun_board.controller;

import board.soyun_board.dto.user.SignupRequestDto;
import board.soyun_board.dto.user.LoginRequestDto;
import board.soyun_board.dto.user.AuthResponseDto;
import board.soyun_board.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Authentication API", description = "Authentication related endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "User Registration", description = "Register a new user with email, password, and name")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        log.info("signup request for email: {}", signupRequestDto.getEmail());
        AuthResponseDto response = authService.signup(signupRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        log.info("login request for email: {}", loginRequestDto.getEmail());
        AuthResponseDto response = authService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Get new access token using refresh token")
    public ResponseEntity<AuthResponseDto> refresh(@RequestHeader("Authorization") String accessToken,
                                                   @RequestParam String refreshToken) {
        log.info("refresh token request");
        AuthResponseDto response = authService.refresh(accessToken, refreshToken);
        return ResponseEntity.ok(response);
    }
}
