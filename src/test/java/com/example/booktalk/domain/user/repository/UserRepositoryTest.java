package com.example.booktalk.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.booktalk.BooktalkApplication;
import com.example.booktalk.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BooktalkApplication.class)
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User savedUser;
    private User user;


    @BeforeEach
    void init() {
        savedUser = userRepository.save(User.builder()
            .email("email@email.com")
            .password("password")
            .build()
        );
    }

    @Nested
    class 조회_테스트 {

        @Test
        void findByEmail() {
            //given
            String email = "email@email.com";
            //when
            Optional<User> result = userRepository.findByEmail(email);
            //then
            assertThat(result.isPresent()).isTrue();
            assertThat(result.get().getEmail()).isEqualTo(savedUser.getEmail());
        }

        @Test
        void findByNickname() {
            //given
            savedUser.updateProfile("descrpiton", "010-1111-2222", "seoul", "nickname",
                "http://path");
            String nickname = "nickname";
            userRepository.save(savedUser);
            //when
            Optional<User> result = userRepository.findByNickname(nickname);
            //then
            assertThat(result.isPresent()).isTrue();
            assertThat(result.get().getNickname()).isEqualTo(savedUser.getNickname());
        }
    }

//    @Nested
//    class 조회테스트_with예외처리 {
//
//        @Test
//        void findUserByEmailWithThrow() {
//            //given
//            String email = "wrong@email.com";
//            //when
//            Optional<User> result = userRepository.findByEmail(email);
//            GlobalException exception = assertThrows(GlobalException.class, ()->{userRepository.findUserByEmailWithThrow(email);});
//            //then
//            assertThat(result.isPresent()).isFalse();
//            assertEquals(UserErrorCode.NOT_FOUND_USER,
//                exception.getErrorCode());
//
//        }
//
//        @Test
//        void findUserByIdWithThrow() {
//            //given
//            //when
//            GlobalException exception = assertThrows(GlobalException.class, ()->{userRepository.findUserByIdWithThrow(100L);});
//            //then
//            assertEquals(UserErrorCode.NOT_FOUND_USER,
//                exception.getErrorCode());
//        }
//    }

}