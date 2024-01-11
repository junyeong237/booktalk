package com.example.booktalk.global.security;

import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetails(String email) {
        User user = userRepository.findUserByEmailWithThrow(email);

        return new UserDetailsImpl(user);
    }


    public UserDetailsImpl loadUserById(Long id) {
        User user = userRepository.findUserByIdWithThrow(id);

        return new UserDetailsImpl(user);
    }


}
