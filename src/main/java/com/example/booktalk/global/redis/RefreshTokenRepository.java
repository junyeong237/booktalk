package com.example.booktalk.global.redis;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    //Optional<RefreshToken> findById(String refreshToken);
}
