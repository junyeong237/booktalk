package com.example.booktalk.domain.kakaoapi.service;


import com.example.booktalk.domain.kakaoapi.dto.KakaoUserInfoDto;
import com.example.booktalk.domain.product.dto.response.ProductApiListRes;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.jwt.JwtUtil;
import com.example.booktalk.global.redis.RefreshTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoApiService {


    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Value("${kakao.key}")
    private String key;

    @Value("${kakao-login.key}")
    private String loginKey;

    String url = "https://dapi.kakao.com/v3/search/book";

    public List<ProductApiListRes> getProductWithKakaoApi(String query) {
        URI uri = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("query", query)
            .encode()
            .build()
            .toUri();
        Map<String, String> hashMap = new HashMap<>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers); //엔티티로 만들기
        ResponseEntity<HashMap> result = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
            HashMap.class);
        List<LinkedHashMap> bookList = (List<LinkedHashMap>) result.getBody().get("documents");

        List<ProductApiListRes> res = bookList.stream()
            .filter(map -> !((Integer) map.get("sale_price")).equals(-1)) // price가 -1일 경우 절판
            .map(map -> {
                Integer salePrice = (Integer) map.get("sale_price");
                String url = (String) map.get("url");
                String name = (String) map.get("title"); // 수정된 부분
                String imageUrl = (String) map.get("thumbnail");
                System.out.println(
                    "sale_price: " + salePrice + ", url: " + url + ", name: " + name + ", imageUrl"
                        + imageUrl);
                return new ProductApiListRes(salePrice.longValue(), url, name, imageUrl);
            })
            .toList();

        return res;
    }

    public void kakaoLogin(String code, HttpServletResponse res) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 반환
        String createAccessToken = jwtUtil.createAccessToken(kakaoUser.getEmail(),
            kakaoUser.getRole());
        String createRefreshToken = jwtUtil.createRefreshToken(kakaoUser.getEmail());

        jwtUtil.addAccessJwtToCookie(createAccessToken, res);
        jwtUtil.addRefreshJwtToCookie(createRefreshToken, res);
        refreshTokenService.saveRefreshToken(createRefreshToken, kakaoUser.getId());
    }

    private String getToken(String code) throws JsonProcessingException {
        log.info("인가코드 : " + code);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com")
            .path("/oauth/token")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", loginKey);//restapi인증키
        body.add("redirect_uri", "https://woogin.shop/api/v2/users/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("accessToken : " + accessToken);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com")
            .path("/v2/user/me")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
            .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.id();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.email();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.email();
                String kakaoUserNickname = UUID.randomUUID().toString().replaceAll("-", "")
                    .substring(0, 10);

                kakaoUser = User.builder()
                    .email(email)
                    .password(encodedPassword)
                    .randomNickname(kakaoUserNickname)
                    .role(UserRoleType.USER)
                    .build();
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }


}
