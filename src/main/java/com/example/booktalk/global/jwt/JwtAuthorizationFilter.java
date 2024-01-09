package com.example.booktalk.global.jwt;

import com.example.booktalk.domain.user.dto.response.UserRes;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);

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
                UserRes responseDto = new UserRes(
                    "유효하지 않은 토큰입니다.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(responseDto));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
