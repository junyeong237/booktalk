package com.example.booktalk.domain.chatRoom.controller;

import com.example.booktalk.domain.chatRoom.dto.ChatRoomCreateReq;
import com.example.booktalk.domain.chatRoom.dto.ChatRoomCreateRes;
import com.example.booktalk.domain.chatRoom.service.ChatRoomServcie;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomServcie chatRoomServcie;

    @PostMapping("/room")
    public ChatRoomCreateRes aa
        (@RequestBody ChatRoomCreateReq req, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatRoomServcie.createChatRoom(userDetails.getUser().getId(), req);

    }

}
