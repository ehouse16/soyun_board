package board.soyun_board.service;

import board.soyun_board.dto.UserSignupRequest;
import board.soyun_board.entity.User;
import board.soyun_board.exception.BoardException;
import board.soyun_board.exception.ErrorCode;
import board.soyun_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserSignupRequest userSignupRequest) {
        //이메일 중복 가입 확인 메서드
        validateDuplicateUser(userSignupRequest.getEmail());

        User user = userSignupRequest.toEntity(passwordEncoder);

        userRepository.save(user);
    }

    private void validateDuplicateUser(String email) {
        User user = userRepository.findByEmail(email);

        if(user != null) {
            throw new BoardException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
}
