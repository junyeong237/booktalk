package com.example.booktalk.domain.chat.repository;

import java.time.LocalDateTime;

public interface ChatRepositoryCustom {
    void deleteOldChats(LocalDateTime threeDaysAgo) ;
}
