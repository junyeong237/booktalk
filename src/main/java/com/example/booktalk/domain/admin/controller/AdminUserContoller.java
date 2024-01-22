package com.example.booktalk.domain.admin.controller;


import com.example.booktalk.domain.admin.service.AdminUserService;
import com.example.booktalk.domain.user.dto.response.UserReportRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


}
