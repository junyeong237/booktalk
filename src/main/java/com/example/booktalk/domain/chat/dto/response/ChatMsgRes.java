package com.example.booktalk.domain.chat.dto.response;

import java.time.LocalDateTime;

public record ChatMsgRes(Long id, String name, String message, LocalDateTime createdAt) {

}
