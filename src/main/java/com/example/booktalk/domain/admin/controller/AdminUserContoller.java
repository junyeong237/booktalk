package com.example.booktalk.domain.admin.controller;


import com.example.booktalk.domain.admin.service.AdminUserService;
import com.example.booktalk.domain.user.dto.response.UserBlockRes;
import com.example.booktalk.domain.user.dto.response.UserReportRes;
import com.example.booktalk.domain.user.dto.response.UserUnblockRes;
import com.example.booktalk.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/users")
@RequiredArgsConstructor
public class AdminUserContoller {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserReportRes> getUser() {
        List<UserReportRes> res = adminUserService.getAllUsers();
        return res;
    }

    @PutMapping("/block/{userId}")
    public UserBlockRes userBlock(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long userId
    ) {
        return adminUserService.createUserBlock(userDetails.getUser().getId(), userId);
    }

    @PutMapping("/unBlock/{userId}")
    public UserUnblockRes userUnBlock(
        @PathVariable Long userId
    ) {
        return adminUserService.createUserUnBlock(userId);
    }


}
