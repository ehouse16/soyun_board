package board.soyun_board.config;

import board.soyun_board.security.JWTCheckFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@Log4j2
@EnableMethodSecurity
public class CustomerSecurityConfig {

    private JWTCheckFilter jwtCheckFilter;

    @Autowired
    private void setJwtCheckFilter(JWTCheckFilter jwtCheckFilter){
        this.jwtCheckFilter = jwtCheckFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("filter chain---------");

        httpSecurity.formLogin(config -> config.disable());
        httpSecurity.logout(config -> config.disable());
        httpSecurity.csrf(config -> config.disable());

        httpSecurity.sessionManagement(config ->
                config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.authorizeHttpRequests(auth -> auth
                // Static resources and frontend routes
                .requestMatchers("/", "/js/**","/css/**", "/static1/**", "/index.html", "/*.js", "/*.json", "/*.ico", "/favicon.ico").permitAll()
                // Frontend React routes - allow all paths that don't start with /api
                .requestMatchers(request -> !request.getServletPath().startsWith("/api/")).permitAll()
                // Public API endpoints
                .requestMatchers("/api/posts/**", "/api/auth/**", "/api/v1/token/**", "/error").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // Protected API endpoints
                .anyRequest().authenticated()
        );

        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        httpSecurity.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://13.124.180.241:8080"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization")); // Allow Authorization header in responses

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
