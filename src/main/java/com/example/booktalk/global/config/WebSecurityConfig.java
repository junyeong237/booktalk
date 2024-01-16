package com.example.booktalk.global.config;

import com.example.booktalk.domain.user.dto.response.UserLogoutRes;
import com.example.booktalk.global.jwt.JwtAuthorizationFilter;
import com.example.booktalk.global.jwt.JwtUtil;
import com.example.booktalk.global.redis.RefreshTokenRepository;
import com.example.booktalk.global.security.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper;

    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // authorize
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper,
            refreshTokenRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                .requestMatchers("/api/v1/users/kakao/**").permitAll()
                .requestMatchers("/api/v1/image/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1").permitAll() //메인페이지
                .requestMatchers(HttpMethod.GET, "/api/v1/products/main").permitAll() //메인페이지
                .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll() //상품목록페이지
                .requestMatchers(HttpMethod.GET, "/api/v1/products/detail/**")
                .permitAll() //상품단건조회페이지
                //.requestMatchers(HttpMethod.GET, "/api/v1/products/register").permitAll() //상품등록페이지
                .requestMatchers(HttpMethod.GET, "/api/v1/users/profile").permitAll() //마이페이지
                //.requestMatchers(HttpMethod.GET, "/api/v1/chats/room").permitAll()//채팅페이지
                .requestMatchers("/api/v1/users/signup").permitAll() //회원가입
                .requestMatchers("/api/v1/users/login").permitAll() //로그인
                .requestMatchers("/save").permitAll()
                .requestMatchers("/api/v1/products/**").permitAll()
                .requestMatchers("/api/v1/products/{productId}/productLikes/**").permitAll()

                .anyRequest().authenticated()
        );
        http.logout(logout -> logout
            .logoutUrl("/api/v1/users/logout")
            .logoutSuccessHandler((request, response, authentication) -> {
                // 로그아웃 성공 시 권한을 비움
                SecurityContextHolder.clearContext();

                // 여기에 추가적인 로그아웃 처리 로직을 넣을 수 있음

                // 응답을 보냄
                response.setStatus(HttpStatus.OK.value());
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter()
                    .write(objectMapper.writeValueAsString(new UserLogoutRes("로그아웃 완료")));
            })
            .deleteCookies("AccessToken", "RefreshToken"));

        // filter
        http.addFilterBefore(jwtAuthorizationFilter(),
            UsernamePasswordAuthenticationFilter.class); // username~ 전에 jwtAuthor 먼저

        return http.build();
    }

}
