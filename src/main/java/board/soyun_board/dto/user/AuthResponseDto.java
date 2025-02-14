// AuthResponseDto.java
package board.soyun_board.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;
}