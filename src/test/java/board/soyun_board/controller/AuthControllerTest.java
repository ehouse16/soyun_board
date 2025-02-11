package board.soyun_board.controller;

import board.soyun_board.dto.UserSignupRequest;
import board.soyun_board.entity.User;
import board.soyun_board.repository.UserRepository;
import board.soyun_board.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Test
    @DisplayName("회원가입 완료")
    void 회원가입_완료() throws Exception{
        //given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("재롱@com")
                .name("재롱")
                .password("재롱123123")
                .build();

        String json = objectMapper.writeValueAsString(request);
        //when
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // Then: 회원가입 후 실제로 DB에 사용자 데이터가 저장되었는지 확인
        User savedUser = userRepository.findByEmail(request.getEmail());
        assertNotNull(savedUser);  // savedUser가 null이 아니면 성공
        assertEquals(request.getEmail(), savedUser.getEmail());
    }
}