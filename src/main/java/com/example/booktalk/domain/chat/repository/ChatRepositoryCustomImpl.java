package com.example.booktalk.domain.chat.repository;

import com.example.booktalk.domain.chat.entity.QChat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QChat chat = QChat.chat;

    @Override
    public void deleteOldChats(LocalDateTime threeDaysAgo) {
                queryFactory
                .delete(chat)
                .where(chat.createdAt.before(threeDaysAgo))
                .execute();
    }
}