package board.soyun_board.controller;

import board.soyun_board.dto.user.UserDTO;
import board.soyun_board.security.JWTUtil;
import board.soyun_board.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/token")
@Log4j2
@RequiredArgsConstructor
public class TokenController {
    private final UserService userService;

    private final JWTUtil jwtUtil;

    @PostMapping("/make")
    public ResponseEntity<Map<String, String>> makeToken(@RequestBody UserDTO userDTO){
        log.info("make token---------");

        UserDTO result = userService.read(userDTO.getEmail(), userDTO.getPassword());

        log.info(result);

        String email = result.getEmail();

        Map<String, Object> dataMap = result.getDataMap();

        String accessToken = jwtUtil.createToken(dataMap,10);
        String refreshToken = jwtUtil.createToken(Map.of("email", email), 60 * 24 * 7);

        log.info("AccessToken : " + accessToken);
        log.info("RefreshToken : " + refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String accessTokenStr,
                                                            @RequestParam("refreshToken") String refreshToken,
                                                            @RequestParam("email") String email){

        log.info("access token with Bearer----------" + accessTokenStr);

        if(accessTokenStr == null || !accessTokenStr.startsWith("Bearer ")){
            return handleException("No Access Token", 400); //400 Bad Request
        }

        if(refreshToken == null){
            return handleException("No Refresh Token", 400); //400 Bad Request
        }

        log.info("refresh token--------------" + refreshToken);

        if(email == null){
            return handleException("No Email", 400); //400 Bad Request
        }

        //Access Token 이 만료되었는지 확인
        String accessToken = accessTokenStr.substring(7);

        try{
            jwtUtil.validateToken(accessToken);

            //아직 만료 기한 남아 있는 상황
            Map<String, String> data = makeData(email, accessToken, refreshToken);

            log.info("Access Token is not expired----------");

            return ResponseEntity.ok(data);

        } catch(io.jsonwebtoken.ExpiredJwtException e) {

            try {
                //Refresh가 필요한 상황
                Map<String, String> newTokenMap = makeNewToken(email, refreshToken);

                return ResponseEntity.ok(newTokenMap);

            } catch (Exception exception) {
                return handleException("REFRESH " + e.getMessage(), 400); //400 Bad Request
            }
        } catch (Exception exception) {
            exception.printStackTrace(); //디버깅용
            return handleException(exception.getMessage(), 400); //400 Bad Request
            }

            //Refesh Token 검증

            //Refresh Token 에서 email 값 추출

            //새로운 Access Token, Refresh Token 생성

            //전송

    }

    private ResponseEntity<Map<String, String>> handleException(String msg, int status){
        return ResponseEntity.status(status).body(Map.of("error", msg));
    }

    private Map<String, String> makeData(String email, String accessToken, String refreshToken){
        return Map.of("email", email, "accessToken", accessToken, "refreshToken", refreshToken);
    }

    private Map<String, String> makeNewToken(String email, String refreshToken) {
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);

        log.info("refresh token claims: " + claims);

        if (!email.equals(claims.get("email").toString())) {
            throw new RuntimeException("Invalid Refresh Token Host");
        }

        //mid를 이용해서 사용자 정보를 다시 확인한 후에 새로운 토큰 생성
        UserDTO userDTO = userService.getByEmail(email);

        Map<String, Object> newClaims = userDTO.getDataMap();

        String newAccessToken = jwtUtil.createToken(newClaims, 10);

        String newRefreshToken = jwtUtil.createToken(Map.of("email", email), 60 * 24 * 7);

        return makeData(email, newAccessToken, newRefreshToken);
    }
}
