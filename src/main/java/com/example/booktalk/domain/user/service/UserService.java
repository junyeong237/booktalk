package com.example.booktalk.domain.user.service;

import com.example.booktalk.domain.user.dto.request.LoginReqDto;
import com.example.booktalk.domain.user.dto.request.SignupReqDto;
import com.example.booktalk.domain.user.dto.response.ProfileResDto;
import com.example.booktalk.domain.user.dto.response.UserResDto;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.exception.AlreadyExistEmailException;
import com.example.booktalk.domain.user.exception.BadLoginException;
import com.example.booktalk.domain.user.exception.InvalidAdminCodeException;
import com.example.booktalk.domain.user.exception.InvalidPasswordCheckException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserResDto signup(SignupReqDto req) {
        String email = req.email();
        String password = passwordEncoder.encode(req.password());
        String passwordCheck = req.passwordCheck();
        String adminToken = req.adminToken();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyExistEmailException(UserErrorCode.ALREADY_EXIST_EMAIL);
        }

        UserRoleType role = UserRoleType.USER;
        if (req.admin()) {
            if (!ADMIN_TOKEN.equals(adminToken)) {
                throw new InvalidAdminCodeException(UserErrorCode.INVALID_ADMIN_CODE);
            }
            role = UserRoleType.ADMIN;
        }
        if (!passwordEncoder.matches(passwordCheck, password)) {
            throw new InvalidPasswordCheckException(UserErrorCode.INVALID_PASSWORD_CHECK);
        }
        String randomNickname = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);

        User user = User.builder()
            .email(email)
            .password(password)
            .role(role)
            .randomNickname(randomNickname)
            .build();

        userRepository.save(user);
        return new UserResDto("회원 가입 완료");
    }

    public UserResDto login(LoginReqDto req, HttpServletResponse res) {
        String email = req.email();
        String password = req.password();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BadLoginException(UserErrorCode.BAD_LOGIN));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadLoginException(UserErrorCode.BAD_LOGIN);
        }
        res.addHeader(JwtUtil.AUTHORIZATION_HEADER,
            jwtUtil.createToken(req.email(), user.getRole()));

        return new UserResDto("로그인 완료");
    }

    public ProfileResDto getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("수정예정"));

        String nickname = user.getNickname();
        String description = user.getDescription();
        String location = user.getLocation();

        return new ProfileResDto(nickname,description,location);
    }
}
