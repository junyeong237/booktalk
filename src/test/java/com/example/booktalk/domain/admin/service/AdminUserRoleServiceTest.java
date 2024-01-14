package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminUserRoleServiceTest {

    @Test
    void 사용자_블록_처리_테스트() {
        // Given
        // UserRepository의 mock 객체를 생성합니다.
        UserRepository userRepository = mock(UserRepository.class);
        // AdminUserRoleService 객체를 생성하고 UserRepository mock 객체를 주입합니다.
        AdminUserRoleService adminUserRoleService = new AdminUserRoleService(userRepository);

        // 사용자 ID를 설정합니다.
        Long userId = 1L;

        // UserRepository의 findById 메서드 호출 시 반환될 가상의 사용자 객체를 생성합니다.
        User mockUser = User.builder()
                .role(UserRoleType.USER) // 원래 역할
                .build();

        // ReflectionTestUtils를 사용해 가상 사용자 객체의 ID를 설정합니다.
        ReflectionTestUtils.setField(mockUser, "id", userId);

        // UserRepository의 findById 메서드 호출 시 반환값을 설정합니다.
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // When
        // 테스트할 메서드를 호출합니다.
        adminUserRoleService.userBlock(userId);

        // Then
        // UserRepository의 findById 메서드가 지정된 userId로 정확히 1번 호출되었는지 검증합니다.
        verify(userRepository, times(1)).findById(userId);

        // 사용자의 역할이 BLOCK으로 업데이트되었는지 확인합니다.
        assertEquals(UserRoleType.BLOCK, mockUser.getRole());
    }
}


