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
    public String userBlock(Long userId){
        User user=findUser(userId);
        UserRoleType role = UserRoleType.BLOCK;
        user.updateRole(role);

        return "차단 되었습니다";
    }
    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는유저가 없습니다."));
    }
}
