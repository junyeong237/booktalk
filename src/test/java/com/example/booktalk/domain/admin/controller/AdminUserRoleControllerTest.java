package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminUserRoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@DisplayName("AdminUserRoleController 테스트")
class AdminUserRoleControllerTest {

    @Mock
    private AdminUserRoleService adminUserRoleService;

    @InjectMocks
    private AdminUserRoleController adminUserRoleController;

    @Test
    @DisplayName("사용자 블록 처리 테스트")
    void 사용자_블록_처리_테스트() {
        // Given
        MockitoAnnotations.initMocks(this);
        Long userId = 1L;

        // When
        adminUserRoleController.userBlock(userId);

        // Then
        verify(adminUserRoleService, times(1)).userBlock(userId);
    }
}
