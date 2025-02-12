package board.soyun_board.service;

import board.soyun_board.component.JwtUtil;
import board.soyun_board.dto.user.UserLoginRequest;
import board.soyun_board.dto.user.UserSignupRequest;
import board.soyun_board.entity.user.User;
import board.soyun_board.exception.BoardException;
import board.soyun_board.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signupSuccess() {
        UserSignupRequest request = UserSignupRequest.builder()
                .email("test@example.com")
                .password("password123")
                .name("Test User")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> userService.signup(request));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() {
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .name("Test User")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("mocked-jwt-token");

        String token = userService.login(request);

        assertEquals("mocked-jwt-token", token);
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 사용자 없음")
    void loginFailUserNotFound() {
        UserLoginRequest request = UserLoginRequest.builder()
                .email("nonexistent@example.com")
                .password("password123")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(null);

        BoardException exception = assertThrows(BoardException.class, () -> userService.login(request));
        assertEquals("USER_NOT_FOUND", exception.getErrorCode().name());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void loginFailInvalidPassword() {
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@example.com")
                .password("wrongpassword")
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .name("Test User")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        BoardException exception = assertThrows(BoardException.class, () -> userService.login(request));
        assertEquals("INVALID_PASSWORD", exception.getErrorCode().name());
    }
}
