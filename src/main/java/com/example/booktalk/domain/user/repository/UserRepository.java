package com.example.booktalk.domain.user.repository;

import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.exception.NotFoundUserException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String name);

    @Cacheable(value = "user", key = "#email")
    default User findUserByEmailWithThrow(String email) {
        return findByEmail(email).orElseThrow(() ->
            new NotFoundUserException(UserErrorCode.NOT_FOUND_USER));
    }

    @Cacheable(value = "user", key = "#userId")
    default User findUserByIdWithThrow(Long userId) {
        return findById(userId).orElseThrow(() ->
            new NotFoundUserException(UserErrorCode.NOT_FOUND_USER));
    }


    @CacheEvict(value = "user", key = "#userId")
    default User findUserByIdWithNotCache(Long userId) {
        return findById(userId).orElseThrow(() ->
            new NotFoundUserException(UserErrorCode.NOT_FOUND_USER));

    }

    List<User> findAll();

    List<User> findByDeletedFalse();

    Optional<User> findByKakaoId(Long kakaoId);
}
