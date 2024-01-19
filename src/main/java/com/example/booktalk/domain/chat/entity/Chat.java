package com.example.booktalk.domain.chat.entity;

import com.example.booktalk.domain.chatRoom.entity.ChatRoom;
import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_CHAT")
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private String content;

    @Builder
    private Chat(ChatRoom chatRoom, User sender, String content) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
    }

}
