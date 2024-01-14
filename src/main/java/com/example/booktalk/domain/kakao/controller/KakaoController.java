package com.example.booktalk.domain.kakao.controller;

import com.example.booktalk.domain.kakao.service.KakaoService;
import com.example.booktalk.global.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/api/v1/users/kakao/callback")
    public String kakaologin(@RequestParam String code, HttpServletResponse res) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);

        Cookie cookie = new Cookie(JwtUtil.ACCESS_TOKEN_HEADER, token.substring(7));
        cookie.setPath("/");
        res.addCookie(cookie);

        return "redirect:/";
    }

}
