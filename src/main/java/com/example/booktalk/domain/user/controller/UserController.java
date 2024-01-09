package com.example.booktalk.domain.user.controller;

import com.example.booktalk.domain.user.dto.request.LoginReq;
import com.example.booktalk.domain.user.dto.request.SignupReq;
import com.example.booktalk.domain.user.dto.response.ProfileRes;
import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserRes> signup(@Valid @RequestBody SignupReq req) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<UserRes> login(@RequestBody LoginReq req, HttpServletResponse res) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.login(req, res));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileRes> getProfile(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.getProfile(userId));
    }
}
