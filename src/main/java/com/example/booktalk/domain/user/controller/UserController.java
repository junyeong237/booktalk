package com.example.booktalk.domain.user.controller;

import com.example.booktalk.domain.user.dto.request.LoginReqDto;
import com.example.booktalk.domain.user.dto.request.SignupReqDto;
import com.example.booktalk.domain.user.dto.response.UserResDto;
import com.example.booktalk.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserResDto> signup (@Valid @RequestBody SignupReqDto req){
        try {
            userService.signup(req);
            return ResponseEntity.ok().body(new UserResDto("회원가입 완료", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UserResDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResDto> login (@RequestBody LoginReqDto req, HttpServletResponse res) {
        try {
            userService.login(req, res);
            return ResponseEntity.ok().body(new UserResDto("로그인 완료", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UserResDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

}
