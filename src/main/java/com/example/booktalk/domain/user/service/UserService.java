package com.example.booktalk.domain.user.service;


import com.example.booktalk.domain.imageFile.service.ImageFileService;
import com.example.booktalk.domain.user.dto.request.UserLoginReq;
import com.example.booktalk.domain.user.dto.request.UserPWUpdateReq;
import com.example.booktalk.domain.user.dto.request.UserProfileReq;
import com.example.booktalk.domain.user.dto.request.UserSignupReq;
import com.example.booktalk.domain.user.dto.response.UserLoginRes;
import com.example.booktalk.domain.user.dto.response.UserOwnProfileGetRes;
import com.example.booktalk.domain.user.dto.response.UserPWUpdateRes;
import com.example.booktalk.domain.user.dto.response.UserProfileGetRes;
import com.example.booktalk.domain.user.dto.response.UserProfileUpdateRes;
import com.example.booktalk.domain.user.dto.response.UserSignupRes;
import com.example.booktalk.domain.user.dto.response.UserWithdrawRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.exception.AlreadyExistEmailException;
import com.example.booktalk.domain.user.exception.BadLoginException;
import com.example.booktalk.domain.user.exception.BlockedUserException;
import com.example.booktalk.domain.user.exception.ForbiddenAccessProfileException;
import com.example.booktalk.domain.user.exception.InvalidAdminCodeException;
import com.example.booktalk.domain.user.exception.InvalidPasswordCheckException;
import com.example.booktalk.domain.user.exception.NicknameDuplicateExcpetion;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import com.example.booktalk.domain.user.exception.WithdrawCheckException;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.jwt.JwtUtil;
import com.example.booktalk.global.redis.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final ImageFileService imageFileService;

    @Value("${admin-token}")
    private String ADMIN_TOKEN;

    public UserSignupRes signup(UserSignupReq req) {
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

        return new UserSignupRes("회원 가입 완료");
    }

    public UserLoginRes login(UserLoginReq req, HttpServletResponse res) {
        String email = req.email();
        String password = req.password();

        User user1 = userRepository.findUserByEmailWithThrow(email);
        User user = userRepository.findUserByIdWithThrow(user1.getId());

        if (user.isDeleted()) {
            throw new BadLoginException(UserErrorCode.NOT_FOUND_USER);
        }

        if (user.getRole() == UserRoleType.BLOCK) {
            throw new BlockedUserException(UserErrorCode.FORBIDDEN_BLOCKED_USER);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadLoginException(UserErrorCode.BAD_LOGIN);
        }

        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        jwtUtil.addAccessJwtToCookie(accessToken, res);
        jwtUtil.addRefreshJwtToCookie(refreshToken, res);
        refreshTokenService.saveRefreshToken(refreshToken, user.getId());

        return new UserLoginRes("로그인 완료");
    }

    @Transactional(readOnly = true)
    public UserOwnProfileGetRes getOwnProfile(Long userId) {
        User user = userRepository.findUserByIdWithThrow(userId);

        String nickname = user.getNickname();
        String description = user.getDescription();
        String location = user.getLocation();
        String profileImagePathUrl = user.getProfileImagePathUrl();
        return new UserOwnProfileGetRes(user.getId(), nickname, user.getEmail(), description,
            location, user.getPhone(), user.getScore(), profileImagePathUrl);

    }

    @Transactional(readOnly = true)
    public UserProfileGetRes getProfile(Long userId) {
        User user = userRepository.findUserByIdWithThrow(userId);

        if (user.isDeleted()) {
            throw new WithdrawCheckException(UserErrorCode.WITHDRAWN_USER);
        }

        Long id = user.getId();
        String nickname = user.getNickname();
        String description = user.getDescription();
        String location = user.getLocation();
        String profileImagePathUrl = user.getProfileImagePathUrl();
        String email = user.getEmail();
        Double scroe = user.getScore();

        return new UserProfileGetRes(id, nickname, description, location, email, scroe,
            profileImagePathUrl);
    }


    public UserProfileUpdateRes updateProfile(Long userId, UserProfileReq req,
        Long userDetailsId, MultipartFile file) throws IOException {
        String description = req.description();
        String phone = req.phone();
        String location = req.location();
        String nickname = req.nickname();

        User user = userRepository.findUserByIdWithNotCache(userId);

        if (!Objects.equals(user.getId(), userDetailsId)) {
            throw new ForbiddenAccessProfileException(UserErrorCode.FORBIDDEN_ACCESS_PROFILE);
        }

        if (!Objects.equals(user.getNickname(), req.nickname())) {
            if (userRepository.findByNickname(req.nickname()).isPresent()) {
                throw new NicknameDuplicateExcpetion(UserErrorCode.NICKNAME_DUPLICATE);
            }
        }

        if (file != null) {
            String profileImagePathUrl = imageFileService.imageUpload(file);
            user.updateProfile(description, phone, location, nickname, profileImagePathUrl);
        } else {
            user.updateProfile(description, phone, location, nickname,
                user.getProfileImagePathUrl());
        }
        //userRepository.save(user);
        return new UserProfileUpdateRes(user.getId(), nickname, user.getEmail(), description,
            location, user.getPhone(), user.getProfileImagePathUrl());
    }

    public UserWithdrawRes withdraw(Long userId) {

        User user = userRepository.findUserByIdWithNotCache(userId);

        user.withdraw();
        //userRepository.save(user);
        return new UserWithdrawRes("탈퇴 완료");
    }

    public UserPWUpdateRes passwordUpdate(UserPWUpdateReq req, Long id) {
        String password = req.password();
        String newPassword = passwordEncoder.encode(req.newPassword());
        String newPasswordCheck = req.newPasswordCheck();

        User user = userRepository.findUserByIdWithNotCache(id);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadLoginException(UserErrorCode.BAD_LOGIN);
        }
        if (!passwordEncoder.matches(newPasswordCheck, newPassword)) {
            throw new InvalidPasswordCheckException(UserErrorCode.INVALID_PASSWORD_CHECK);
        }
        user.updatePassword(newPassword);
        return new UserPWUpdateRes("비밀번호 변경 완료");
    }

    public void UserWithdrawCheck(Long userId) {
        User user = userRepository.findUserByIdWithThrow(userId);

        if (user.isDeleted()) {
            throw new WithdrawCheckException(UserErrorCode.WITHDRAWN_USER);
        }
    }
}
