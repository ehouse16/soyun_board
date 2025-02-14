package board.soyun_board.service;

import board.soyun_board.dto.user.SignupRequestDto;
import board.soyun_board.dto.user.LoginRequestDto;
import board.soyun_board.dto.user.AuthResponseDto;
import board.soyun_board.entity.user.Role;
import board.soyun_board.entity.user.User;
import board.soyun_board.exception.BoardException;
import board.soyun_board.exception.ErrorCode;
import board.soyun_board.repository.UserRepository;
import board.soyun_board.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Transactional
    public AuthResponseDto signup(SignupRequestDto signupRequestDto) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(signupRequestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new BoardException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // Create new user
        User user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .name(signupRequestDto.getName())
                .role(Role.USER) // Default role
                .build();

        userRepository.save(user);

        // Generate tokens
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user.getEmail());

        return AuthResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new BoardException(ErrorCode.USER_NOT_FOUND));

        // Verify password
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BoardException(ErrorCode.BAD_CREDENTIALS);
        }

        // Generate tokens
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user.getEmail());

        return AuthResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponseDto refresh(String accessToken, String refreshToken) {
        // Remove "Bearer " prefix if present
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        try {
            // Validate refresh token
            Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
            String email = claims.get("email").toString();

            // Get user details
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BoardException(ErrorCode.USER_NOT_FOUND));

            // Generate new tokens
            String newAccessToken = createAccessToken(user);
            String newRefreshToken = createRefreshToken(email);

            return AuthResponseDto.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();

        } catch (Exception e) {
            throw new BoardException(ErrorCode.INVALID_TOKEN);
        }
    }

    private String createAccessToken(User user) {
        Map<String, Object> claims = Map.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "role", user.getRole()
        );
        return jwtUtil.createToken(claims, 30); // 30 minutes
    }

    private String createRefreshToken(String email) {
        return jwtUtil.createToken(Map.of("email", email), 60 * 24 * 7); // 7 days
    }
}