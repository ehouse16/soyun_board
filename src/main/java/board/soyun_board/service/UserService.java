package board.soyun_board.service;

import board.soyun_board.dto.user.UserDTO;
import board.soyun_board.entity.user.User;
import board.soyun_board.exception.BoardException;
import board.soyun_board.exception.ErrorCode;
import board.soyun_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO read(String email, String password){
        Optional<User> result = userRepository.findByEmail(email);

        User user = result.orElseThrow(()-> new BoardException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new BoardException(ErrorCode.BAD_CREDENTIALS);
        }
        return new UserDTO(user);
    }

    public UserDTO getByEmail(String email){
        Optional<User> result = userRepository.findByEmail(email);

        User user = result.orElseThrow(()-> new BoardException(ErrorCode.USER_NOT_FOUND));

        return new UserDTO(user);
    }
}
