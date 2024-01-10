package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserRoleService {
    private final UserRepository userRepository;
    @Transactional
    public void userBlock(Long userId){
        User user=findUser(userId);
        UserRoleType role = UserRoleType.BLOCK;
        user.updateRole(role);
    }
    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는유저가 없습니다."));
    }
}
