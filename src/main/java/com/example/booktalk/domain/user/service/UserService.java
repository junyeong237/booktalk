package com.example.booktalk.domain.user.service;


import com.example.booktalk.domain.user.dto.request.LoginReq;
import com.example.booktalk.domain.user.dto.request.SignupReq;
import com.example.booktalk.domain.user.dto.response.ProfileRes;
import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.exception.AlreadyExistEmailException;
import com.example.booktalk.domain.user.exception.BadLoginException;
import com.example.booktalk.domain.user.exception.ForbiddenAccessProfileException;
import com.example.booktalk.domain.user.exception.InvalidAdminCodeException;
import com.example.booktalk.domain.user.exception.InvalidPasswordCheckException;
import com.example.booktalk.domain.user.exception.NotFoundUserException;
import com.example.booktalk.domain.user.exception.NotMatchPasswordException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.jwt.JwtUtil;
import com.example.booktalk.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserRes signup(SignupReq req) {
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
        return new UserRes("회원 가입 완료");
    }

    public UserRes login(LoginReq req, HttpServletResponse res) {
        String email = req.email();
        String password = req.password();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BadLoginException(UserErrorCode.BAD_LOGIN));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadLoginException(UserErrorCode.BAD_LOGIN);
        }
        res.addHeader(JwtUtil.AUTHORIZATION_HEADER,
            jwtUtil.createToken(req.email(), user.getRole()));

        return new UserRes("로그인 완료");
    }

    public ProfileRes getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("수정예정"));

        String nickname = user.getNickname();
        String description = user.getDescription();
        String location = user.getLocation();

        return new ProfileRes(nickname,description,location);
    }

    public UserResDto updateProfile(Long userId, ProfileReqdto req, UserDetailsImpl userDetails) {
        String password = req.password();
        String newPassword = passwordEncoder.encode(req.newPassword());
        String newPasswordCheck = req.newPasswordCheck();
        String description = req.description();
        String phone = req.phone();
        String location = req.location();
        String nickname = req.nickname();
        User loginUser = userDetails.getUser();

        User user = userRepository.findById(userId).orElseThrow(()->new NotFoundUserException(UserErrorCode.NOT_FOUND_USER));

        if(!Objects.equals(user.getId(), loginUser.getId())){
            throw new ForbiddenAccessProfileException(UserErrorCode.FORBIDDEN_ACCESS_PROFILE);
        }

        if(!passwordEncoder.matches(newPasswordCheck,newPassword)){
            throw new InvalidPasswordCheckException(UserErrorCode.INVALID_PASSWORD_CHECK);
        }

        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new NotMatchPasswordException(UserErrorCode.NOT_MATCH_PASSWORD);
        }
        user.updateProfile(newPassword,description,phone,location,nickname);
        userRepository.save(user);

        return new UserResDto("수정 완료");
    }


}
