package board.soyun_board.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        // Only apply filter to API requests that require authentication
        if (!path.startsWith("/api/") ||
                path.startsWith("/api/auth/") ||
                path.startsWith("/api/posts/") ||
                path.startsWith("/api/v1/token/") ||
                path.equals("/error")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        log.info("JWTCheckFilter doFilter----------");

        log.info("RequestURI: " + request.getRequestURI());

        String headerStr = request.getHeader("Authorization");

        log.info("headerStr: " + headerStr);

        //Access Token 이 없는 경우
        if(headerStr == null || !headerStr.startsWith("Bearer ")){
            handleException(response, new Exception("ACCESS TOKEN NOT FOUND"));
            return;
        }

        String accessToken = headerStr.substring(7);

        try{
            java.util.Map<String, Object> tokenMap = jwtUtil.validateToken(accessToken);

            //토큰 검증 결과에 문제가 없었다
            log.info("tokenMap: " + tokenMap);

            String email = tokenMap.get("email").toString();

            //권한이 여러 개인 경우에는 , 로 구분해서 처리
            String[] roles = tokenMap.get("role").toString().split(",");

            //토큰 검증 결과를 이용해서 Authentication 객체를 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    new CustomUserPrincipal(email), null, Arrays.stream(roles)
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList())
            );

            //SecurityContextHolder에 Authentication 객체를 저장
            //이후에 SecurityContextHolder를 이용해서 Authentcation 객체를 꺼내서 사용할 수 있다
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch(Exception e){
            //문제가 발생했다면
            handleException(response, e);

        }
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().println("{\"error\":\""+e.getMessage()+"\"}");
    }
}
