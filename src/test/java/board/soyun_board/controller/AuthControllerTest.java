package board.soyun_board.controller;

import board.soyun_board.dto.user.LoginResponse;
import board.soyun_board.dto.user.UserLoginRequest;
import board.soyun_board.dto.user.UserSignupRequest;
import board.soyun_board.entity.user.Role;
import board.soyun_board.entity.user.User;
import board.soyun_board.repository.UserRepository;
import board.soyun_board.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // 테스트 전 기존 데이터 초기화
    }

    @Test
    @DisplayName("회원가입 완료")
    void 회원가입_완료() throws Exception {
        //given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("test@test.com")
                .name("test")
                .password("password123")
                .build();

        String json = objectMapper.writeValueAsString(request);
        //when
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // Then: 회원가입 후 실제로 DB에 사용자 데이터가 저장되었는지 확인
        User savedUser = userRepository.findByEmail(request.getEmail());
        assertNotNull(savedUser);  // savedUser가 null이 아니면 성공
        assertEquals(request.getEmail(), savedUser.getEmail());
    }

    @Test
    @DisplayName("로그인 성공")
    void 로그인_성공() throws Exception {
        // Given: 회원 가입 처리
        User user = User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123")) // 저장 시 암호화
                .role(Role.USER)
                .name("test")
                .build();
        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .email("test@test.com")
                .password("test123") // 평문 비밀번호 사용
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        // When & Then: 로그인 요청 및 응답 검증
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty()) // JWT 토큰 반환 확인
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 잘못된 비밀번호")
    void 로그인_실패_잘못된_비밀번호() throws Exception {
        // Given: 회원 저장
        User user = User.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .role(Role.USER)
                .name("test")
                .build();
        userRepository.save(user);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .email("test@test.com")
                .password("wrong_password") // 잘못된 비밀번호 입력
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        // When & Then: 로그인 요청 시 400 응답 및 에러 메시지 확인
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 비밀번호 입니다"))
                .andDo(print());
    }


}