package com.example.booktalk.global.config;

import com.example.booktalk.domain.user.dto.response.UserLogoutRes;
import com.example.booktalk.global.jwt.JwtAuthorizationFilter;
import com.example.booktalk.global.jwt.JwtUtil;
import com.example.booktalk.global.redis.RefreshTokenRepository;
import com.example.booktalk.global.security.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
                .requestMatchers("api/v1").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                .requestMatchers("/api/v1/users/kakao/**").permitAll()
                .requestMatchers("/api/v1/image/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1").permitAll() //메인페이지
                .requestMatchers(HttpMethod.GET, "/api/v1/products/main").permitAll() //메인페이지
                .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll() //상품목록페이지
                .requestMatchers(HttpMethod.GET, "/api/v1/products/detail/**")
                .permitAll() //상품단건조회페이지
                .requestMatchers(HttpMethod.GET, "api/v1/reviews/**").permitAll()
                //.requestMatchers(HttpMethod.GET, "/api/v1/products/register").permitAll() //상품등록페이지
                //.requestMatchers(HttpMethod.GET, "/api/v1/users/profile").permitAll() //마이페이지
                //.requestMatchers(HttpMethod.GET, "/api/v1/chats/room").permitAll()//채팅페이지
                .requestMatchers("/api/v1/users/signup").permitAll() //회원가입
                .requestMatchers("/api/v1/users/login").permitAll() //로그인
                .requestMatchers("/save").permitAll()
                .requestMatchers("/api/v1/products/**").permitAll()
                .requestMatchers("/api/v1/products/{productId}/productLikes/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                //hasRole 자체가 "ROLE_"을 포함하고 있어서 UserRoleType.ADMIN을 사용하려면 String타입의 "ROLE_ADMIN"에서 "ADMIN"만 사용필요
                .requestMatchers("/api/v1/reports").permitAll()


                .anyRequest().authenticated()
        );
        http.logout(logout -> logout
            .logoutUrl("/api/v1/users/logout")
            .logoutSuccessUrl("/api/v1")  // Add a leading slash here
            .logoutSuccessHandler((request, response, authentication) -> {
                // Clear privileges upon successful logout
                SecurityContextHolder.clearContext();

                // Additional logout processing logic can be added here
                log.info("Logout successful. Redirecting to /api/v1");
                // send response
                response.setStatus(HttpStatus.OK.value());
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter()
                    .write(objectMapper.writeValueAsString(new UserLogoutRes("Logout complete")));
            })
            .deleteCookies("AccessToken", "RefreshToken"));

        // filter
        http.addFilterBefore(jwtAuthorizationFilter(),
            UsernamePasswordAuthenticationFilter.class); // username~ 전에 jwtAuthor 먼저

        return http.build();
    }

}
