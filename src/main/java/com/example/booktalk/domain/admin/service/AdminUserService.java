package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.user.dto.response.UserReportRes;
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
    public List<UserReportRes> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users
            .stream()
            .map(user -> new UserReportRes(user.getId(), user.getNickname(),user.getReportCount(),user.getRole())
            ).toList();
    }
}
