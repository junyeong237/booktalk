package com.example.booktalk.domain.user.controller;


import com.example.booktalk.domain.user.dto.request.UserLoginReq;
import com.example.booktalk.domain.user.dto.request.UserProfileReq;
import com.example.booktalk.domain.user.dto.request.UserSignupReq;
import com.example.booktalk.domain.user.dto.response.UserLoginRes;
import com.example.booktalk.domain.user.dto.response.UserProfileGetRes;
import com.example.booktalk.domain.user.dto.response.UserProfileUpdateRes;
import com.example.booktalk.domain.user.dto.response.UserSignupRes;
import com.example.booktalk.domain.user.service.UserService;
import com.example.booktalk.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileGetRes> getProfile(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileUpdateRes> updateProfile(@PathVariable(name = "userId") Long userId,
        @RequestBody UserProfileReq req, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.updateProfile(userId, req, userDetails.getUser().getId()));

    }
}
