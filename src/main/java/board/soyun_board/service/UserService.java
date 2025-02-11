package board.soyun_board.service;

import board.soyun_board.component.JwtUtil;
import board.soyun_board.dto.user.UserLoginRequest;
import board.soyun_board.dto.user.UserSignupRequest;
import board.soyun_board.entity.user.User;
import board.soyun_board.exception.BoardException;
import board.soyun_board.exception.ErrorCode;
import board.soyun_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(UserSignupRequest userSignupRequest) {
        //이메일 중복 가입 확인 메서드
        validateDuplicateUser(userSignupRequest.getEmail());

        User user = userSignupRequest.toEntity(passwordEncoder);

        user.encodePassword(passwordEncoder);
        userRepository.save(user);
    }

    private void validateDuplicateUser(String email) {
        User user = userRepository.findByEmail(email);

        if(user != null) {
            throw new BoardException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional(readOnly = true)
    public String login(UserLoginRequest userLoginRequest){
        //이메일을 통해 사용자 정보 조회
        User user = userRepository.findByEmail(userLoginRequest.getEmail());

        if(user == null) {
            throw new BoardException(ErrorCode.USER_NOT_FOUND);
        }

        //비밀번호 확인
        if(!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())){
            throw new BoardException(ErrorCode.INVALID_PASSWORD);
        }

        //로그인 성공 시 JWT 토큰 생성
        return jwtUtil.generateToken(user.getEmail());
    }
}
