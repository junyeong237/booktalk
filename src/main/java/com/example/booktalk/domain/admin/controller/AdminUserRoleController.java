package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminUserRoleController {

    private final AdminUserRoleService adminUserRoleService;
    @PutMapping("/user/{userId}")
    public void userBlock(
            @PathVariable Long userId
    ) {
        adminUserRoleService.userBlock(userId);
    }
}
