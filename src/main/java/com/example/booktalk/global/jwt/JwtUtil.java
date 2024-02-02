package com.example.booktalk.global.jwt;

import com.example.booktalk.domain.user.entity.UserRoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    public static final String ACCESS_TOKEN_HEADER = "AccessToken";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public final long ACCESS_TOKEN_TIME = 60 * 1000 * 60;
    public final long REFRESH_TOKEN_TIME = 60 * 1000L * 60 * 24 * 3;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    //public final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String createAccessToken(String email, UserRoleType role) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }


    public String createRefreshToken(String email) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public void addAccessJwtToCookie(String token, HttpServletResponse res) {
        token = URLEncoder.encode(token, StandardCharsets.UTF_8)
            .replaceAll("\\+", "%20"); // encode bc there has to be no space in Cookie Value

        Cookie cookie = new Cookie(ACCESS_TOKEN_HEADER, token); // Name-Value
        cookie.setPath("/");
        cookie.setMaxAge((int) ACCESS_TOKEN_TIME);

        // add Cookie to Response
        res.addCookie(cookie);
    }

    public void addRefreshJwtToCookie(String token, HttpServletResponse res) {
        token = URLEncoder.encode(token, StandardCharsets.UTF_8)
            .replaceAll("\\+", "%20"); // encode bc there has to be no space in Cookie Value

        Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, token); // Name-Value
        cookie.setPath("/");
        cookie.setMaxAge((int) REFRESH_TOKEN_TIME);

        // add Cookie to Response
        res.addCookie(cookie);
    }

    public String getTokenFromRequest(HttpServletRequest req, String header) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(header)) {
                    return URLDecoder.decode(cookie.getValue().replaceAll("Bearer%20", ""),
                        StandardCharsets.UTF_8); // decode value
                }
            }
        }
        return null;
    }

}
