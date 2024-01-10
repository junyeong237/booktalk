package com.example.booktalk.domain.chat.repository;

import com.example.booktalk.domain.chat.entity.Chat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByChatRoomId(Long roomId);
}
