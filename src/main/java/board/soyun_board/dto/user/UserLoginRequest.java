package board.soyun_board.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginRequest {
    private String email;
    private String password;
}
