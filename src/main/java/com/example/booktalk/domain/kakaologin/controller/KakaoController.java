package com.example.booktalk.domain.kakaologin.controller;

import com.example.booktalk.domain.kakaologin.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Controller
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/api/v2/users/kakao/callback")
    public String kakaologin(@RequestParam String code, HttpServletResponse res) throws JsonProcessingException {
        kakaoService.kakaoLogin(code, res);

        return "index";
    }

}
