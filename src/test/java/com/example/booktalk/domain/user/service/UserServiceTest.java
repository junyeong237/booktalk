package com.example.booktalk.domain.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.example.booktalk.domain.user.dto.request.UserLoginReq;
import com.example.booktalk.domain.user.dto.request.UserProfileReq;
import com.example.booktalk.domain.user.dto.request.UserSignupReq;
import com.example.booktalk.domain.user.dto.response.UserLoginRes;
import com.example.booktalk.domain.user.dto.response.UserProfileGetRes;
import com.example.booktalk.domain.user.dto.response.UserProfileUpdateRes;
import com.example.booktalk.domain.user.dto.response.UserSignupRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.exception.AlreadyExistEmailException;
import com.example.booktalk.domain.user.exception.BadLoginException;
import com.example.booktalk.domain.user.exception.NotMatchPasswordException;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.jwt.JwtUtil;
import com.example.booktalk.global.redis.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Spy
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private RefreshTokenService refreshTokenService;
    private User user1;
    private User user;

    @BeforeEach
    void init() {
        user1 = User.builder()
            .email("email@email.com")
            .password("password")
            .build();
        ReflectionTestUtils.setField(user1, "id", 1L); // user1의 id를 1로 설정

        user = User.builder()
            .email("email@email.com")
            .password("wrongPassword")
            .build();
        ReflectionTestUtils.setField(user, "id", 2L); // user의 id를 2로 설정
    }

    @Nested
    class 회원가입_테스트 {

        @Test
        void 회원가입_성공() {
            //given
            UserSignupReq req = new UserSignupReq(
                "email@email.com",
                "password",
                "password",
                false,
                "invalidToken");
            given(userRepository.findByEmail(req.email())).willReturn(
                Optional.empty());//중복 이메일이 없음을 설정

            //when
            when(passwordEncoder.matches(req.passwordCheck(),
                passwordEncoder.encode(req.password()))).thenReturn(true);
            when(userRepository.save(any())).thenReturn(user);
            UserSignupRes res = userService.signup(req);

            //then
            assertNotNull(res.message());
        }

        @Test
        void 회원가입_실패() {
            //given
            UserSignupReq req = new UserSignupReq(
                "email@email.com",
                "password",
                "password",
                false,
                "invalidToken");
            given(userRepository.findByEmail(req.email())).willReturn(Optional.ofNullable(user1));

            //when
            when(passwordEncoder.matches(req.passwordCheck(),
                passwordEncoder.encode(req.password()))).thenReturn(true);

            //then
            assertThatThrownBy(() -> userService.signup(req))
                .isInstanceOf(AlreadyExistEmailException.class)
                .hasMessage("이미 가입된 이메일입니다.");
        }

    }

    @Nested
    class 로그인_테스트 {

        @Test
        void 로그인_성공() {
            // given
            UserLoginReq req = new UserLoginReq("email@email.com", "password");
            given(userRepository.findUserByEmailWithThrow(req.email())).willReturn(user);
            given(passwordEncoder.matches(req.password(), user.getPassword())).willReturn(true);

            // when
            HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
            UserLoginRes res = userService.login(req, response);

            // then
            assertNotNull(res.message());
        }

        @Test
        void 로그인_실패_비밀번호불일치() {
            // given
            UserLoginReq req = new UserLoginReq("email@email.com", "wrongPassword");
            given(userRepository.findUserByEmailWithThrow(req.email())).willReturn(user);
            given(passwordEncoder.matches(req.password(), user.getPassword())).willReturn(false);

            // then
            assertThatThrownBy(
                () -> userService.login(req, Mockito.mock(HttpServletResponse.class)))
                .isInstanceOf(BadLoginException.class)
                .hasMessage("로그인 실패");
        }

    }

    @Nested
    class 프로필_테스트 {

        @DisplayName("프로필 조회")
        @Test
        void getProfile() {
            // given
            Long userId = 2L;
            ReflectionTestUtils.setField(user, "nickname", "TestNickname");
            ReflectionTestUtils.setField(user, "description", "TestDescription");
            ReflectionTestUtils.setField(user, "location", "TestLocation");
            given(userRepository.findUserByIdWithThrow(userId)).willReturn(user);

            // when
            UserProfileGetRes res = userService.getProfile(userId);

            // then
            assertNotNull(res.nickname());
            assertNotNull(res.description());
            assertNotNull(res.location());
        }

        @DisplayName("프로필 수정")
        @Test
        void updateProfile() {
            // given
            Long userId = 2L;
            Long userDetailsId = 2L;
            UserProfileReq req = new UserProfileReq(
                "password",
                "newPassword",
                "newPassword",
                "newDescription",
                "newPhone",
                "newLocation",
                "newNickname"
            );
            given(userRepository.findUserByIdWithThrow(userId)).willReturn(user);
            given(passwordEncoder.matches(req.password(), user.getPassword())).willReturn(true);
            given(passwordEncoder.matches(req.newPasswordCheck(),
                passwordEncoder.encode(req.newPassword()))).willReturn(true);

            // when
            UserProfileUpdateRes res = userService.updateProfile(userId, req, userDetailsId);

            // then
            assertNotNull(res.message());
        }

        @Test
        void 프로필_수정_실패_비밀번호불일치() {
            // given
            Long userId = 2L;
            Long userDetailsId = 2L;
            UserProfileReq req = new UserProfileReq(
                "wrongPassword",
                "newPassword",
                "newPassword",
                "newDescription",
                "newPhone",
                "newLocation",
                "newNickname"
            );
            given(userRepository.findUserByIdWithThrow(userId)).willReturn(user);
            given(passwordEncoder.matches(req.password(), user.getPassword())).willReturn(false);

            // then
            assertThatThrownBy(() -> userService.updateProfile(userId, req, userDetailsId))
                .isInstanceOf(NotMatchPasswordException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
        }
    }
}