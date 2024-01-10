package com.example.booktalk.domain.imageFile.controller;


import com.example.booktalk.domain.imageFile.service.ImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/image")
public class ImageFileController {

    private ImageFileService imageFileService;

    @Autowired
    public ImageFileController(ImageFileService imageFileService) {

        this.imageFileService = imageFileService;
    }

    @GetMapping("/editor")
    public String editorPage(){
        return "editor";
    }
    @PostMapping
    @ResponseBody
    public Map<String, Object> imageUpload(MultipartRequest request) throws Exception {
        Map<String, Object> responseData = new HashMap<>();
        try {
            String s3Url = imageFileService.imageUpload(request);
            responseData.put("uploaded", true);
            responseData.put("url", s3Url);
            return responseData;
        } catch (IOException e) {
            responseData.put("uploaded", false);
            return responseData;
        }
    }


}