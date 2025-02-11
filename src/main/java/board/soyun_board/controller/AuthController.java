package board.soyun_board.controller;

import board.soyun_board.dto.UserSignupRequest;
import board.soyun_board.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원가입과 로그인", description = "회원가입과 로그인에 대한 API 입니다")
public class AuthController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserSignupRequest request) {
        userService.signup(request);

        return ResponseEntity.ok("회원가입 성공");
    }
}
