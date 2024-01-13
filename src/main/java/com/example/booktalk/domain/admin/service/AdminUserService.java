package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserRes> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users
            .stream()
            .map(user -> new UserRes(user.getId(), user.getNickname())
            ).toList();
    }
}
