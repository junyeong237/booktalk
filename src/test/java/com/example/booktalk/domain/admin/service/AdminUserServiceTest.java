package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        when(userRepository.findAll()).thenReturn(mockUsers);

        // When
        List<UserRes> result = adminUserService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("user1", result.get(0).name());
        assertEquals(2L, result.get(1).id());
        assertEquals("user2", result.get(1).name());
    }
}
