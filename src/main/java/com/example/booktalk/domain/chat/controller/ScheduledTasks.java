package com.example.booktalk.domain.chat.controller;

import com.example.booktalk.domain.chat.repository.ChatRepository;
import com.example.booktalk.domain.chat.service.ChatService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableScheduling // 스케줄링을 활성화합니다.
public class ScheduledTasks {

    private final ChatService chatService;

    // 자정마다 deleteChat 메서드를 실행합니다.
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteChatPeriodically() {
        chatService.deleteChat();

    }
}