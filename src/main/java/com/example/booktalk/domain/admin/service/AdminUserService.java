package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.user.dto.response.UserBlockRes;
import com.example.booktalk.domain.user.dto.response.UserReportRes;
import com.example.booktalk.domain.user.dto.response.UserUnblockRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.exception.NotBlockSelfException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserReportRes> getAllUsers() {
        List<User> users = userRepository.findByDeletedFalse();
        return users
            .stream()
            .map(user -> new UserReportRes(user.getId(), user.getNickname(), user.getReportCount(),
                user.getRole())
            ).toList();
    }

    public UserBlockRes createUserBlock(Long myUserId, Long userId) {
        User user = userRepository.findUserByIdWithNotCache(userId);
        if (Objects.equals(myUserId, userId)) {
            throw new NotBlockSelfException(UserErrorCode.FORBIDDEN_NOT_BLOCK_SELF);
        }
        if (Objects.equals(user.getRole(), UserRoleType.ADMIN)) {
            throw new NotBlockSelfException(UserErrorCode.FORBIDDEN_NOT_BLOCK_ADMIN);
        }
        UserRoleType role = UserRoleType.BLOCK;
        user.updateRole(role);

        return new UserBlockRes("차단 되었습니다");
    }

    public UserUnblockRes createUserUnBlock(Long userId) {
        User user = userRepository.findUserByIdWithNotCache(userId);
        UserRoleType role = UserRoleType.USER;
        user.updateRole(role);

        return new UserUnblockRes("차단해제 되었습니다");
    }
}
