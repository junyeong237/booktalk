package com.example.booktalk.domain.chatRoom.controller;

import com.example.booktalk.domain.chatRoom.dto.ChatRoomCreateReq;
import com.example.booktalk.domain.chatRoom.dto.ChatRoomCreateRes;
import com.example.booktalk.domain.chatRoom.dto.ChatRoomDeleteRes;
import com.example.booktalk.domain.chatRoom.service.ChatRoomServcie;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ChatRoomCreateRes createChatRoom
        (@RequestBody ChatRoomCreateReq req, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatRoomServcie.createChatRoom(userDetails.getUser().getId(), req);

    }

    @DeleteMapping("/room/{roomId}")
    public ChatRoomDeleteRes createChatRoom
        (@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return chatRoomServcie.deleteChatRoom(roomId, userDetails.getUser().getId());

    }

}
