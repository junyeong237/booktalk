package com.example.booktalk.domain.user.service;

import com.example.booktalk.domain.user.dto.request.SignupReqDto;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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

        User user = User.builder()
            .email(email)
            .password(password)
            .role(role)
            .build();

        userRepository.save(user);
    }
}
