package com.example.booktalk.domain.chatRoom.controller;

import com.example.booktalk.domain.chatRoom.dto.ChatRoomCreateReq;
import com.example.booktalk.domain.chatRoom.dto.ChatRoomCreateRes;
import com.example.booktalk.domain.chatRoom.dto.ChatRoomDeleteRes;
import com.example.booktalk.domain.chatRoom.dto.ChatRoomListRes;
import com.example.booktalk.domain.chatRoom.service.ChatRoomServcie;
import com.example.booktalk.domain.notify.NotificationService;
import com.example.booktalk.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/chatRooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomServcie chatRoomServcie;
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<ChatRoomListRes>> getChatRoomList
        (@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatRoomListRes> res = chatRoomServcie.getChatRoomList(userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @PostMapping
    public ResponseEntity<ChatRoomCreateRes> createChatRoom
        (@RequestBody ChatRoomCreateReq req, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ChatRoomCreateRes res = chatRoomServcie.createChatRoom(userDetails.getUser().getId(), req);
        notificationService.notifyMessage(req.receiverId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ChatRoomDeleteRes> deleteChatRoom
        (@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ChatRoomDeleteRes res = chatRoomServcie.deleteChatRoom(roomId,
            userDetails.getUser().getId());

        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

}
