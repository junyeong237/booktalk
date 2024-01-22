package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminUserRoleService;
import com.example.booktalk.domain.user.dto.response.UserBlockRes;
import com.example.booktalk.domain.user.dto.response.UserUnblockRes;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin")
@RequiredArgsConstructor
public class AdminUserRoleController {

    private final AdminUserRoleService adminUserRoleService;
    @PutMapping("/block/{userId}")
    public UserBlockRes userBlock(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @PathVariable Long userId
    ) {
        return adminUserRoleService.userBlock(userDetails.getUser().getId(),userId);
    }

    @PutMapping("/unBlock/{userId}")
    public UserUnblockRes userUnBlock(
            @PathVariable Long userId
    ) {
        return adminUserRoleService.userUnBlock(userId);
    }

}
