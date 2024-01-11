package com.example.booktalk.domain.user.repository;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.exception.NotFoundUserException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String name);

    default User findUserByEmailWithThrow(String email) {
        return findByEmail(email).orElseThrow(()->
            new NotFoundUserException(UserErrorCode.NOT_FOUND_USER));
    }

    default User findUserByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new NotFoundUserException(UserErrorCode.NOT_FOUND_USER));
    }
    List<User> findAll();
}
