package com.example.booktalk.domain.front;

import com.example.booktalk.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class FrontUserController {
    private final UserService userService;

    @GetMapping("/booktalk/users/profile")
    public String myProfilePage() {
        return "profile";
    }

    @GetMapping("/booktalk/users/{userId}/profile")
    public String otherProfilePage(@PathVariable Long userId, Model model) {
        userService.UserWithdrawCheck(userId);
        model.addAttribute("userId", userId);
        return "otherProfile";
    }

}
