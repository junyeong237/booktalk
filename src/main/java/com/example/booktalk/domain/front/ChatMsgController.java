package com.example.booktalk.domain.front;

import com.example.booktalk.global.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatMsgController {

    @GetMapping("/api/v1/chats/room/{roomId}")
    public String chatPage(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("username", userDetails.getUser().getNickname());
        return "chat";
    }

    @GetMapping("/api/v1/chats/room/list")
    public String chatRoomList(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        model.addAttribute("userId", userDetails.getUser().getId());
        return "roomList";
    }

}
