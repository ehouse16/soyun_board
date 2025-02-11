package board.soyun_board.learning;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PasswordEncoderTest {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    @DisplayName("패스워드 암호화")
    public void 패스워드_암호화() throws Exception{
        //given
        String password = "재롱";

        //when
        String encodePassword = passwordEncoder.encode(password);
        String encodePassword2 = passwordEncoder.encode(password);

        assertThat(encodePassword).isNotEqualTo(encodePassword2);
    }

    @Test
    @DisplayName("암호화된 비밀번호 매치")
    public void 암호화된_비밀번호_매치() throws Exception{
        //given
        String password = "재롱";

        //when
        String encodePassword = passwordEncoder.encode(password);

        //then
        assertThat(passwordEncoder.matches(password, encodePassword)).isTrue();
    }
}
