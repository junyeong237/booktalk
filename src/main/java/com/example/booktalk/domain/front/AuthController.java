package com.example.booktalk.domain.front;

import com.example.booktalk.domain.kakaoapi.service.KakaoApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final KakaoApiService kakaoApiService;

    @Value("${kakao-login.key}")
    private String loginKey;

    @GetMapping("/booktalk/users/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/booktalk/users/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/api/v2/users/kakao/callback")
    public String kakaologin(
        @RequestParam String code, HttpServletResponse res
    ) throws JsonProcessingException {
        kakaoApiService.kakaoLogin(code, res);

        return "index";
    }

    @GetMapping("/getKakaoLoginUrl")
    public String getKakaoLoginUrl() {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + loginKey + "&redirect_uri=https://woogin.shop/api/v2/users/kakao/callback";
        return "redirect:" + kakaoLoginUrl;
    }

}
