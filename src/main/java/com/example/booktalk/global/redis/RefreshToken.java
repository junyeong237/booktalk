package com.example.booktalk.global.redis;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 1209600) // redis 사용, 유효기간 14일
//@RedisHash 어노테이션은 Spring Data Redis에서 사용되며, Redis에 데이터를 저장하기 위해
//해시 자료구조를 사용하는데에 적용됩니다.

public class RefreshToken {

    @Id
    private String refreshToken;

    private Long userId;


    @Builder
    private RefreshToken(Long userId, String refreshToken) {

        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
