package board.soyun_board.controller;

import board.soyun_board.component.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class JwtUtilTest {
    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secretKey;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil(secretKey);
    }

    @Test
    @DisplayName("토큰 생성 및 검증 테스트")
    void 토큰_생성_및_검증_테스트(){
        //given
        String email = "test@test.com";

        //when
        String token = jwtUtil.generateToken(email);

        //then
        assertThat(token).isNotNull();
        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.getEmailFromToken(token)).isEqualTo(email);
    }

    @Test
    @DisplayName("잘못된 토큰 검증 테스트")
    void 잘못된_토큰_검증_테스트(){
        //given
        String invalidToken = "invalid.token.value";

        //when
        boolean isValid = jwtUtil.validateToken(invalidToken);

        //then
        assertThat(isValid).isFalse();
    }


}
