package com.example.booktalk.global.redis;

import com.example.booktalk.domain.user.exception.NotFoundRefreshTokenException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    
    default RefreshToken findRefreshTokenByIdWithThrow(String refreshToken) {
        return findById(refreshToken).orElseThrow(() ->
            new NotFoundRefreshTokenException(UserErrorCode.NOT_FOUND_REFRESH_TOKEN));
    }
}
