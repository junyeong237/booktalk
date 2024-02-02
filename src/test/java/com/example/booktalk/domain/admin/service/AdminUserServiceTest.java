package com.example.booktalk.domain.admin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.booktalk.domain.user.dto.response.UserBlockRes;
import com.example.booktalk.domain.user.dto.response.UserReportRes;
import com.example.booktalk.domain.user.dto.response.UserUnblockRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.exception.NotBlockSelfException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsersTest() {
        // Given
        List<User> mockUsers = new ArrayList<>();

        User mockUser1 = User.builder()
            .randomNickname("user1")
            .email("email1@example.com")
            .password("password1")
            .build();
        ReflectionTestUtils.setField(mockUser1, "id", 1L);
        mockUsers.add(mockUser1);

        User mockUser2 = User.builder()
            .randomNickname("user2")
            .email("email2@example.com")
            .password("password2")
            .build();
        ReflectionTestUtils.setField(mockUser2, "id", 2L);
        mockUsers.add(mockUser2);

        // Stubbing the userRepository.findAll() method
        when(userRepository.findByDeletedFalse()).thenReturn(mockUsers);

        // When
        List<UserReportRes> result = adminUserService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("user1", result.get(0).name());
        assertEquals(2L, result.get(1).id());
        assertEquals("user2", result.get(1).name());
    }

    @Test
    @DisplayName("유저 차단 테스트")
    void createUserBlockTest() {
        // Given
        Long myUserId = 1L;
        Long blockUserId = 2L;

        User mockUser1 = User.builder()
            .randomNickname("user1")
            .email("email1@example.com")
            .password("password1")
            .build();
        ReflectionTestUtils.setField(mockUser1, "id", 1L);

        when(userRepository.findUserByIdWithNotCache(blockUserId)).thenReturn(mockUser1);

        // When
        UserBlockRes result = adminUserService.createUserBlock(myUserId, blockUserId);

        // Then
        assertEquals("차단 되었습니다", result.msg());
        assertEquals(UserRoleType.BLOCK, mockUser1.getRole());
    }

    @Test
    @DisplayName("자기자신_차단시_예외발생_테스트")
    void createUserBlock_자기자신_차단시_예외발생() {
        // Given
        Long myUserId = 1L;

        // When & Then
        NotBlockSelfException exception = assertThrows(NotBlockSelfException.class,
            () -> adminUserService.createUserBlock(myUserId, myUserId));
        assertEquals(UserErrorCode.FORBIDDEN_NOT_BLOCK_SELF, exception.getErrorCode());
    }

    @Test
    @DisplayName("관리자차단시_예외발생_테스트")
    void createUserBlock_관리자차단시_예외발생() {
        // Given
        Long myUserId = 1L;
        Long adminId = 2L;

        User admin = User.builder()
            .randomNickname("admin")
            .email("email1@example.com")
            .role(UserRoleType.ADMIN)
            .password("password1")
            .build();
        ReflectionTestUtils.setField(admin, "id", adminId);

        when(userRepository.findUserByIdWithNotCache(adminId)).thenReturn(admin);

        // When & Then
        NotBlockSelfException exception = assertThrows(NotBlockSelfException.class,
            () -> adminUserService.createUserBlock(myUserId, adminId));
        assertEquals(UserErrorCode.FORBIDDEN_NOT_BLOCK_ADMIN, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저 차단 해제 테스트")
    void createUserUnblock() {
        // Given
        Long userId = 1L;
        User mockUser1 = User.builder()
            .randomNickname("user1")
            .email("email1@example.com")
            .password("password1")
            .build();
        ReflectionTestUtils.setField(mockUser1, "id", userId);

        when(userRepository.findUserByIdWithNotCache(userId)).thenReturn(mockUser1);

        // When
        UserUnblockRes result = adminUserService.createUserUnBlock(userId);

        // Then
        assertEquals("차단해제 되었습니다", result.msg());
        assertEquals(UserRoleType.USER, mockUser1.getRole());
    }


}
