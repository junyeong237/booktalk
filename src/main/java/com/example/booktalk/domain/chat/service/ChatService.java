package com.example.booktalk.domain.chat.service;

import com.example.booktalk.domain.chat.dto.request.ChatMsgReq;
import com.example.booktalk.domain.chat.dto.response.ChatListRes;
import com.example.booktalk.domain.chat.dto.response.ChatMsgRes;
import com.example.booktalk.domain.chat.entity.Chat;
import com.example.booktalk.domain.chat.repository.ChatRepository;
import com.example.booktalk.domain.chatRoom.entity.ChatRoom;
import com.example.booktalk.domain.chatRoom.repository.ChatRoomRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;


    public ChatMsgRes createChat(Long roomId, ChatMsgReq req, StompHeaderAccessor session) {

        ChatRoom chatRoom = findChatRoom(roomId);
        var tempName = session.getSessionAttributes().get("name");
        User sender = findUserByName(tempName.toString());

        Chat chat = Chat.builder()
            .chatRoom(chatRoom)
            .sender(sender)
            .content(req.message())
            .build();

        chatRepository.save(chat);

        return new ChatMsgRes(chat.getId(), chat.getSender().getNickname(), chat.getContent(),
            chat.getCreatedAt());

    }

    @Transactional(readOnly = true)
    public List<ChatListRes> getChatList(Long roomId) {

        List<Chat> chatList = chatRepository.findAllByChatRoomId(roomId);

        return chatList.stream().map(
                chat -> new ChatListRes(chat.getId(), chat.getSender().getNickname(), chat.getContent(),
                    chat.getCreatedAt()))
            .toList();
    }


    private ChatRoom findChatRoom(Long roomId) {

        return chatRoomRepository.findById(roomId).orElseThrow
            (() -> new IllegalArgumentException("해당하는 채팅방이 없습니다."));

    }

    private User findUserByName(String name) {
        return userRepository.findByNickname(name)
            .orElseThrow(() -> new IllegalArgumentException("해당하는유저가 없습니다."));
    }
}
