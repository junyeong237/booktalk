package com.example.booktalk.global.intercept;

import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class ChannelInboundInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //STOMP 메시지가 전송되기 전에 호출되는 메서드로,
        // 메시지와 관련된 StompHeaderAccessor를 활용하여 특별한 동작을 수행

        StompHeaderAccessor header = StompHeaderAccessor.wrap(message);
        //메시지에 대한 StompHeaderAccessor를 생성합니다. 이를 통해 STOMP 메시지의 헤더에 접근할 수 있게 됩니다.

        if (StompCommand.CONNECT.equals(header.getCommand())) {
            //connect라면 name값을 꺼내서 sessionAttributes에 넣기.
            Map<String, Object> attributes = header.getSessionAttributes();
            attributes.put("name", header.getFirstNativeHeader("name"));
            
            header.setSessionAttributes(attributes);
            //수정된 속성을 다시 StompHeaderAccessor에 설정합니다.
        }
        return message;
    }
}