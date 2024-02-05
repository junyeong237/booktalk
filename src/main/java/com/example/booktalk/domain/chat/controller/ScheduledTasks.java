package com.example.booktalk.domain.chat.controller;

import com.example.booktalk.domain.chat.service.ChatService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableScheduling // 스케줄링을 활성화합니다.
public class ScheduledTasks {

    private final ChatService chatService;

    // 3일 마다 deleteChat 메서드를 실행합니다.
    @Scheduled(fixedRate = 60*1000*60*24*3)
    public void deleteChatPeriodically() {
        chatService.deleteChat();
    }
}