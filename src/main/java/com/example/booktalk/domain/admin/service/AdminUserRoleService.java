package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.user.dto.response.UserBlockRes;
import com.example.booktalk.domain.user.dto.response.UserUnblockRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.exception.NotBlockSelfException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserRoleService {
    private final UserRepository userRepository;
    @Transactional
    public UserBlockRes userBlock(Long myUserId, Long userId){
        User user=userRepository.findUserByIdWithThrow(userId);
        if(Objects.equals(myUserId, userId)){
            throw new NotBlockSelfException(UserErrorCode.FORBIDDEN_NOT_BLOCK_SELF);
        }
        if(Objects.equals(user.getRole(),UserRoleType.ADMIN)){
            throw new NotBlockSelfException(UserErrorCode.FORBIDDEN_NOT_BLOCK_ADMIN);
        }
        UserRoleType role = UserRoleType.BLOCK;
        user.updateRole(role);

        return new UserBlockRes("차단 되었습니다");
    }

    public UserUnblockRes userUnBlock(Long userId) {
        User user=userRepository.findUserByIdWithThrow(userId);
        UserRoleType role = UserRoleType.USER;
        user.updateRole(role);

        return new UserUnblockRes("차단해제 되었습니다");
    }
}
