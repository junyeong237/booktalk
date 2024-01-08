package com.example.booktalk.domain.user.service;

import com.example.booktalk.domain.user.dto.request.LoginReqDto;
import com.example.booktalk.domain.user.dto.request.SignupReqDto;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
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
    public void signup(SignupReqDto req) {
        String email = req.email();
        String password = passwordEncoder.encode(req.password());
        String passwordCheck = req.passwordCheck();
        String adminToken = req.adminToken();

        if(userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입한 이메일 입니다.");
        }

        UserRoleType role =  UserRoleType.USER;
        if(req.admin()){
            if(!ADMIN_TOKEN.equals(adminToken)){
                throw new IllegalArgumentException("관리자 인증 번호가 틀렸습니다.");
            }
            role = UserRoleType.ADMIN;
        }
        if(!passwordEncoder.matches(passwordCheck,password)){
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인을 일치시켜주세요");
        }
        String randomNickname = UUID.randomUUID().toString();

        User user = User.builder()
            .email(email)
            .password(password)
            .role(role)
            .randomNickname(randomNickname)
            .build();

        userRepository.save(user);
    }

    public void login(LoginReqDto req, HttpServletResponse res) {
        String email = req.email();
        String password = req.password();

        User user = userRepository.findByEmail(email)
            .orElseThrow(()->new IllegalArgumentException("이메일을 확인해주세요"));
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("비밀번호를 확인해주세요");
        }
        res.addHeader(JwtUtil.AUTHORIZATION_HEADER,
            jwtUtil.createToken(req.email(), user.getRole()));
    }
}
