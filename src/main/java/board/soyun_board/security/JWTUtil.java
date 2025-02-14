package board.soyun_board.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {
    @Value("${jwt.secret}")
    private String key;

    public String createToken(Map<String, Object> valueMap, int min){
        SecretKey secretKey = null;

        try {
            secretKey = Keys.hmacShaKeyFor(key.getBytes("UTF-8"));
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Jwts.builder().header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .expiration((Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())))
                .claims(valueMap)
                .signWith(secretKey) .
                compact();
    }

    public Map<String, Object> validateToken(String token){
        SecretKey secretKey = null;

        try {
            secretKey = Keys.hmacShaKeyFor(key.getBytes("UTF-8"));
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        Claims claims = Jwts.parser().verifyWith(secretKey) .build() .parseSignedClaims(token) .getPayload();

        log.info("claims: " + claims);

        return claims;
    }
}
