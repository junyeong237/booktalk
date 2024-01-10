package com.example.booktalk.global.jwt;

import com.example.booktalk.domain.user.dto.response.UserLoginRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.global.redis.RefreshToken;
import com.example.booktalk.global.redis.RefreshTokenRepository;
import com.example.booktalk.global.security.UserDetailsImpl;
import com.example.booktalk.global.security.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.getTokenFromRequest(request, JwtUtil.AUTHORIZATION_HEADER);
        if (Objects.nonNull(token)) {
            if (jwtUtil.validateToken(token)) {
                Claims info = jwtUtil.getUserInfoFromToken(token);
                // email -> search user
                String email = info.getSubject();
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // -> put this in userDetails
                UserDetailsImpl userDetails = userDetailsService.getUserDetails(email);
                // ->  put this in authentication principal
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
                // -> put this in securityContent
                context.setAuthentication(authentication);
                // -> put this in SecurityContextHolder
                SecurityContextHolder.setContext(context);
                // -> now you can search with @AuthenticationPrincipal

            } else {

                //TODO 리프레시토큰 유효성 검사
                //if(
                String refreshtoekn = jwtUtil.getTokenFromRequest(request,
                    JwtUtil.REFRESHTOKEN_HEADER);//
                //여기서 얻어낸 토큰이 유효하면 유효하다면
                //Token이 BEARER
                if (jwtUtil.validateToken(refreshtoekn)) {
                    log.error("리프레시토큰 유효성 체크");

                    refreshtoekn = JwtUtil.BEARER_PREFIX + refreshtoekn;

                    RefreshToken rf = refreshTokenRepository.findById(refreshtoekn)
                        .orElseThrow(() -> new IllegalArgumentException("해당하는 어쩌구 오류"));

                    Long userId = rf.getUserId();
                    UserDetailsImpl userDetails = userDetailsService.loadUserById(userId);

                    User user = userDetails.getUser();

                    String accessToken = jwtUtil.createAccessToken(user.getEmail(),
                        user.getRole());

                    jwtUtil.addAccessJwtToCookie(accessToken, response);
                    accessToken = accessToken.substring(7);
                    log.error(accessToken);
                    Claims info = jwtUtil.getUserInfoFromToken(accessToken);
                    // email -> search user
                    String email = info.getSubject();
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    // -> put this in userDetails
                    UserDetailsImpl userDetails1 = userDetailsService.getUserDetails(email);
                    // ->  put this in authentication principal
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails1,
                        null, userDetails1.getAuthorities());
                    // -> put this in securityContent
                    context.setAuthentication(authentication);
                    // -> put this in SecurityContextHolder
                    SecurityContextHolder.setContext(context);
                    // -> now you can search with @AuthenticationPrincipal

                } else {
                    log.error("리프레시토큰 유효성하지 않음");
                }


            }
        } else {
            UserLoginRes res = new UserLoginRes(
                "유효하지 않은 토큰입니다.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(res));
            return;
        }
        filterChain.doFilter(request, response);
    }


}
