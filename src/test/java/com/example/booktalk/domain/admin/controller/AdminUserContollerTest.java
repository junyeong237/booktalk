package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminUserService;
import com.example.booktalk.domain.user.dto.response.UserRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("AdminUserContoller 테스트")
class AdminUserContollerTest {

    @Mock
    private AdminUserService adminUserService;

    @InjectMocks
    private AdminUserContoller adminUserContoller;

    @Test
    @DisplayName("모든 사용자 조회 테스트")
    void 사용자_조회_테스트() {
        // Given
        MockitoAnnotations.initMocks(this);
        List<UserRes> mockUserList = Arrays.asList(
                new UserRes(1L, "User1"),
                new UserRes(2L, "User2")
        );
        when(adminUserService.getAllUsers()).thenReturn(mockUserList);

        // When
        List<UserRes> result = adminUserContoller.getUser();

        // Then
        assertEquals(mockUserList.size(), result.size());
        verify(adminUserService, times(1)).getAllUsers();
    }
}
