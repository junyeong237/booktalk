package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminUserRoleService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminUserRoleController {

    private final AdminUserRoleService adminUserRoleService;
    @PutMapping("/user/{userId}/block")
    public String userBlock(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long userId
    ) {
        return adminUserRoleService.userBlock(userDetails.getUser().getId(),userId);
    }

    @PutMapping("/user/{userId}/unBlock")
    public String Unblock(
            @PathVariable Long userId
    ) {
        return adminUserRoleService.userUnBlock(userId);
    }

}
