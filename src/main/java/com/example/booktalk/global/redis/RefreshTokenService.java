package com.example.booktalk.global.redis;


import com.example.booktalk.global.jwt.JwtUtil;
import com.example.booktalk.global.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshToken saveRefreshToken(String refreshToken, Long userId) {
        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .userId(userId)
            .refreshToken(refreshToken)
            .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenEntity;
    }


}
