package com.example.booktalk.domain.chat.controller;

import com.example.booktalk.domain.chat.dto.request.ChatMsgReq;
import com.example.booktalk.domain.chat.dto.response.ChatEnterRes;
import com.example.booktalk.domain.chat.dto.response.ChatExitRes;
import com.example.booktalk.domain.chat.dto.response.ChatListRes;
import com.example.booktalk.domain.chat.dto.response.ChatMsgRes;
import com.example.booktalk.domain.chat.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/chats/rooms")
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}/enter")
    @SendTo("/topic/{roomId}")
    public ResponseEntity<ChatEnterRes> enter(@DestinationVariable String roomId,
        StompHeaderAccessor session) {
        ChatEnterRes res = new ChatEnterRes(
            session.getSessionAttributes().get("name") + "님께서 입장하셨습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @MessageMapping("/{roomId}/exit")
    @SendTo("/topic/{roomId}")
    public ResponseEntity<ChatExitRes> exit(@DestinationVariable String roomId,
        StompHeaderAccessor session) {
        ChatExitRes res = new ChatExitRes(
            session.getSessionAttributes().get("name") + "님께서 퇴장하셨습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @MessageMapping("/{roomId}/chat")
    @SendTo("/topic/{roomId}")
    public ResponseEntity<ChatMsgRes> chat(@DestinationVariable String roomId, ChatMsgReq req,
        StompHeaderAccessor session) throws Exception {

        ChatMsgRes res = chatService.createChat(Long.parseLong(roomId), req, session);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatListRes>> getChatList(@PathVariable String roomId) {

        List<ChatListRes> res = chatService.getChatList(Long.parseLong(roomId));

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


}
