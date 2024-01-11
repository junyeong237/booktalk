package com.example.booktalk.global.security;

import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetails(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email + "을 찾을 수 없습니다."));
        return new UserDetailsImpl(user);
    }


    public UserDetailsImpl loadUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 유저 아이디입니다."));

        return new UserDetailsImpl(user);
    }


}
