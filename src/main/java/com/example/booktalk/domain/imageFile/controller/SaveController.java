package com.example.booktalk.domain.imageFile.controller;

import com.example.booktalk.domain.imageFile.dto.ImageCreateReq;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SaveController {

    private final ImageFileService imageFileService;
    @PostMapping("/save")
    public String saveLogic(ImageCreateReq req) {
        imageFileService.createImage(req);


        return "redirect:/";
    }
}
