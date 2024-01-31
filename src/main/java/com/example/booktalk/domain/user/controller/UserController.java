package com.example.booktalk.domain.user.controller;


import com.example.booktalk.domain.user.dto.request.UserLoginReq;
import com.example.booktalk.domain.user.dto.request.UserPWUpdateReq;
import com.example.booktalk.domain.user.dto.request.UserProfileReq;
import com.example.booktalk.domain.user.dto.request.UserSignupReq;
import com.example.booktalk.domain.user.dto.request.UserWithdrawReq;
import com.example.booktalk.domain.user.dto.response.UserLoginRes;
import com.example.booktalk.domain.user.dto.response.UserOwnProfileGetRes;
import com.example.booktalk.domain.user.dto.response.UserPWUpdateRes;
import com.example.booktalk.domain.user.dto.response.UserProfileGetRes;
import com.example.booktalk.domain.user.dto.response.UserProfileUpdateRes;
import com.example.booktalk.domain.user.dto.response.UserSignupRes;
import com.example.booktalk.domain.user.dto.response.UserWithdrawRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.service.UserService;
import com.example.booktalk.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/users")
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
    public ResponseEntity<UserProfileGetRes> getProfile(
        @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileUpdateRes> updateProfile(@PathVariable Long userId,
        @Valid @RequestPart("req") UserProfileReq req,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(value = "upload",required = false) MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.updateProfile(userId, req, userDetails.getUser().getId(), file));

    }

    @PutMapping("/withdraw")
    public ResponseEntity<UserWithdrawRes> withdraw(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.withdraw(userDetails.getUser().getId()));
    }

    @PutMapping("/password/{userId}")
    public ResponseEntity<UserPWUpdateRes> passwordUpdate(@RequestBody UserPWUpdateReq req,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.passwordUpdate(req, userDetails.getUser().getId()));
    }

    @GetMapping("/role")
    public UserRoleType getUserRole(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUser().getRole();
    }

    @GetMapping("/nickname")
    public String getUsernickname(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userDetails.getUser().getNickname();
    }
}
