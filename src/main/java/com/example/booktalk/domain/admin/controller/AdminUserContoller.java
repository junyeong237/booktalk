package com.example.booktalk.domain.admin.controller;


import com.example.booktalk.domain.admin.service.AdminUserService;
import com.example.booktalk.domain.user.dto.response.UserRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserContoller {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<UserRes> getUser() {
        List<UserRes> res = adminUserService.getAllUsers();
        return res;
    }


}
