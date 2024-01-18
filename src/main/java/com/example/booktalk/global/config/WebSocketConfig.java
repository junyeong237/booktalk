package com.example.booktalk.global.config;

import com.example.booktalk.global.intercept.ChannelInboundInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChannelInboundInterceptor channelInboundInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); ////발행자가 "/topic"의 경로로 메시지를 주면 구독자들에게 전달
        config.setApplicationDestinationPrefixes(
            "/app"); /// 발행자가 "/app"의 경로로 메시지를 주면 가공을 해서 구독자들에게 전달
    }   //즉 "/app/..." 경로로 메시지를 보내면
    //이 메시지는 @MessageMapping 어노테이션이 붙은 곳으로 향하게 된다.

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
        //// 커넥션을 맺는 경로 설정. 만약 WebSocket을 사용할 수 없는 브라우저라면 다른 방식을 사용하도록 설정
        //버젼낮은 브라우져에서도 사용가능
        //registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInboundInterceptor);
    }

}
