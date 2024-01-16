package com.example.booktalk.domain.user.controller;


import com.example.booktalk.domain.user.dto.response.UserPWUpdateRes;
import com.example.booktalk.domain.user.dto.request.UserLoginReq;
import com.example.booktalk.domain.user.dto.request.UserPWUpdateReq;
import com.example.booktalk.domain.user.dto.request.UserProfileReq;
import com.example.booktalk.domain.user.dto.request.UserSignupReq;
import com.example.booktalk.domain.user.dto.request.UserWithdrawReq;
import com.example.booktalk.domain.user.dto.response.UserLoginRes;
import com.example.booktalk.domain.user.dto.response.UserOwnProfileGetRes;
import com.example.booktalk.domain.user.dto.response.UserProfileGetRes;
import com.example.booktalk.domain.user.dto.response.UserProfileUpdateRes;
import com.example.booktalk.domain.user.dto.response.UserSignupRes;
import com.example.booktalk.domain.user.dto.response.UserWithdrawRes;
import com.example.booktalk.domain.user.service.UserService;
import com.example.booktalk.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupRes> signup(@Valid @RequestBody UserSignupReq req) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginRes> login(@RequestBody UserLoginReq req,
        HttpServletResponse res) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.login(req, res));
    }

    @PostMapping("/logout")
    public void logout() {

    }

    @GetMapping
    public ResponseEntity<UserOwnProfileGetRes> getOwnProfile(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.getOwnProfile(userDetails.getUser().getId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileGetRes> getProfile(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileUpdateRes> updateProfile(@PathVariable Long userId,
                                                              @RequestPart("req") UserProfileReq req,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @RequestParam("upload") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.updateProfile(userId, req, userDetails.getUser().getId(),file));

    }

    @PutMapping("/withdraw")
    public ResponseEntity<UserWithdrawRes> withdraw(@RequestBody UserWithdrawReq req,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.withdraw(req, userDetails.getUser().getId()));
    }

    @PutMapping("/password/{userId}")
    public ResponseEntity<UserPWUpdateRes> passwordUpdate(@RequestBody UserPWUpdateReq req,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.passwordUpdate(req,userDetails.getUser().getId()));
    }
}
