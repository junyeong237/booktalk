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
        User user=userRepository.findUserByIdWithThrow(userId);
        UserRoleType role = UserRoleType.BLOCK;
        user.updateRole(role);

        return "차단 되었습니다";
    }

    public String userUnBlock(Long userId) {
        User user=userRepository.findUserByIdWithThrow(userId);
        UserRoleType role = UserRoleType.USER;
        user.updateRole(role);

        return "차단해제 되었습니다";
    }
}
