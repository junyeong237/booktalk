package com.example.booktalk.domain.notify;

import com.example.booktalk.domain.user.repository.UserRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;

    // 메시지 알림
    public SseEmitter subscribe(Long userId) {

        // 1. 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // 2. 연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. 저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

        // 4. 연결 종료 처리
        sseEmitter.onCompletion(
            () -> NotificationController.sseEmitters.remove(userId));    // sseEmitter 연결이 완료될 경우
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(
            userId));        // sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(
            userId));        // sseEmitter 연결에 오류가 발생할 경우

        return sseEmitter;
    }

    // 채팅 수신 알림 - receiver 에게
    public void notifyMessage(Long receiverId) {
        // 5. 수신자 정보 조회
        //User user = userRepository.findByNickname(receiver).get();

        // 6. 수신자 정보로부터 id 값 추출
        Long userId = receiverId;

        // 7. Map 에서 userId 로 사용자 검색
        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitterReceiver = NotificationController.sseEmitters.get(userId);
            // 8. 알림 메시지 전송 및 해체
            try {
                sseEmitterReceiver.send(
                    SseEmitter.event().name("createChatRoom").data("사용자에게 채팅요청이 왔습니다."));
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }

    public void notifyReviewMessage(Long receiverId) {
        // 5. 수신자 정보 조회
        //User user = userRepository.findByNickname(receiver).get();

        // 6. 수신자 정보로부터 id 값 추출
        Long userId = receiverId;

        // 7. Map 에서 userId 로 사용자 검색
        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitterReceiver = NotificationController.sseEmitters.get(userId);
            // 8. 알림 메시지 전송 및 해체
            try {
                sseEmitterReceiver.send(
                        SseEmitter.event().name("createReview").data("상품에 리뷰가 달렸습니다."));
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(userId);
            }
        }
    }

}