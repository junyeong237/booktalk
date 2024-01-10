package com.example.booktalk.domain.chatRoom.repository;

import com.example.booktalk.domain.chatRoom.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Boolean existsByReceiverIdAndSenderId(Long receiverId, Long SenderId);

    Optional<ChatRoom> findByReceiverIdAndSenderId(Long receiverId, Long SenderId);

}
