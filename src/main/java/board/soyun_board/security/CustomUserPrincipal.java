package board.soyun_board.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

@RequiredArgsConstructor
public class CustomUserPrincipal implements Principal{
    private final String email;

    @Override
    public String getName() {
        return email;
    }
}
