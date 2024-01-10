package com.example.booktalk.domain.chat.dto.response;

import java.time.LocalDateTime;

public record ChatListRes(Long id, String name, String content, LocalDateTime createdAt) {

}
