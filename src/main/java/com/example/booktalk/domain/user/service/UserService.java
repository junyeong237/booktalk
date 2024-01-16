package com.example.booktalk.domain.user.service;


import com.example.booktalk.domain.imageFile.dto.response.ImageCreateRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageGetRes;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
import com.example.booktalk.domain.user.dto.request.UserLoginReq;
import com.example.booktalk.domain.user.dto.request.UserPWUpdateReq;
import com.example.booktalk.domain.user.dto.request.UserProfileReq;
import com.example.booktalk.domain.user.dto.request.UserSignupReq;
import com.example.booktalk.domain.user.dto.request.UserWithdrawReq;
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
import com.example.booktalk.domain.user.exception.NotMatchPasswordException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.jwt.JwtUtil;
import com.example.booktalk.global.redis.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final ImageFileService imageFileService;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

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

        User user = userRepository.findUserByEmailWithThrow(email);

        if (user.isDeleted()) {
            throw new BadLoginException(UserErrorCode.NOT_FOUND_USER);
        }

        if(user.getRole() == UserRoleType.BLOCK){
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

    public UserOwnProfileGetRes getOwnProfile(Long userId) {
        User user = userRepository.findUserByIdWithThrow(userId);

        String nickname = user.getNickname();
        String description = user.getDescription();
        String location = user.getLocation();
        ImageGetRes imageGetRes =imageFileService.getProfileImage(userId);
        return new UserOwnProfileGetRes(user.getId(), nickname, user.getEmail(), description,
            location, user.getPhone(),imageGetRes);

    }

    public UserProfileGetRes getProfile(Long userId) {
        User user = userRepository.findUserByIdWithThrow(userId);

        String nickname = user.getNickname();
        String description = user.getDescription();
        String location = user.getLocation();
        ImageGetRes imageGetRes =imageFileService.getProfileImage(userId);

        return new UserProfileGetRes(nickname, description, location,imageGetRes);
    }

    public UserProfileUpdateRes updateProfile(Long userId, UserProfileReq req,
        Long userDetailsId,MultipartFile file) throws IOException {
        String password = req.password();
//        String newPassword = passwordEncoder.encode(req.newPassword());
//        String newPasswordCheck = req.newPasswordCheck();
        String description = req.description();
        String phone = req.phone();
        String location = req.location();
        String nickname = req.nickname();

        User user = userRepository.findUserByIdWithThrow(userId);

        if (!Objects.equals(user.getId(), userDetailsId)) {
            throw new ForbiddenAccessProfileException(UserErrorCode.FORBIDDEN_ACCESS_PROFILE);
        }

//        if (!passwordEncoder.matches(newPasswordCheck, newPassword)) {
//            throw new InvalidPasswordCheckException(UserErrorCode.INVALID_PASSWORD_CHECK);
//        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new NotMatchPasswordException(UserErrorCode.NOT_MATCH_PASSWORD);
        }
        user.updateProfile(description, phone, location, nickname);
        userRepository.save(user);

        ImageCreateRes imageCreateRes;
        if (file != null && !file.isEmpty()) {
            imageCreateRes = imageFileService.updateProfileImage(userId, file);
        }else {
            imageCreateRes=imageFileService.deleteProfileImage(user);
        }

        return new UserProfileUpdateRes(user.getId(), nickname, user.getEmail(), description,
            location, user.getPhone(),imageCreateRes);
    }


    @Transactional
    public UserWithdrawRes withdraw(UserWithdrawReq req, Long id) {
        String password = req.password();
        String passwordCheck = req.passwordCheck();

        User user = userRepository.findUserByIdWithThrow(id);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadLoginException(UserErrorCode.BAD_LOGIN);
        }
        if (!Objects.equals(password, passwordCheck)) {
            throw new InvalidPasswordCheckException(UserErrorCode.INVALID_PASSWORD_CHECK);
        }
        user.withdraw();
        return new UserWithdrawRes("탈퇴 완료");
    }

    @Transactional
    public UserPWUpdateRes passwordUpdate(UserPWUpdateReq req, Long id) {
        String password = req.password();
        String newPassword = passwordEncoder.encode(req.newPassword());
        String newPasswordCheck = req.newPasswordCheck();

        User user = userRepository.findUserByIdWithThrow(id);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadLoginException(UserErrorCode.BAD_LOGIN);
        }
        if (!passwordEncoder.matches(newPasswordCheck, newPassword)) {
            throw new InvalidPasswordCheckException(UserErrorCode.INVALID_PASSWORD_CHECK);
        }
        user.updatePassword(newPassword);
        return new UserPWUpdateRes("비밀번호 변경 완료");
    }
}
