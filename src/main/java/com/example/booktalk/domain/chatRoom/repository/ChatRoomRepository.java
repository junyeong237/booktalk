package com.example.booktalk.domain.chatRoom.repository;

import com.example.booktalk.domain.chatRoom.entity.ChatRoom;
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Boolean existsByReceiverIdAndSenderId(Long receiverId, Long SenderId);

    Optional<ChatRoom> findByReceiverIdAndSenderId(Long receiverId, Long SenderId);

    List<ChatRoom> findAllByReceiverIdOrSenderId(Long receiverId, Long SenderId);

    default ChatRoom findChatRoomByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }

}
