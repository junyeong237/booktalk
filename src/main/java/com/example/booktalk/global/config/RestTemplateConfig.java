package com.example.booktalk.global.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
            //외부 API 호출 시 일정 시간이 지나도 응답이 없을 때 강제 종료 설정
            .setConnectTimeout(Duration.ofSeconds(10)) // 5초
            .setReadTimeout(Duration.ofSeconds(10)) // 5초
            .build();
    }
  
}

