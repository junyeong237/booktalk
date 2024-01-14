package com.example.booktalk.domain.kakaoapi;


import com.example.booktalk.domain.product.dto.response.ProductApiListRes;
import com.example.booktalk.domain.product.repository.ProductRepository;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoApiService {


    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    @Value("${kakao.key}")
    private String key;

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
                System.out.println("sale_price: " + salePrice + ", url: " + url);
                return new ProductApiListRes(salePrice.longValue(), url);
            })
            .toList();

        return res;
    }


}
