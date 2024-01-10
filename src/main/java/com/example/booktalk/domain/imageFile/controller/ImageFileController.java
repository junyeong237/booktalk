package com.example.booktalk.domain.imageFile.controller;


import com.example.booktalk.domain.imageFile.dto.request.ImageCreateReq;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageFileController {

    private ImageFileService imageFileService;



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

    @PostMapping("/save")
    public String createImage(ImageCreateReq req) {
        imageFileService.createImage(req);


        return "editor";
    }


    @GetMapping("/list")
    public String listPage(Model model) {


        model.addAttribute("ContentList", imageFileService.getImages());


        return "list";
    }
    @GetMapping("/{id}")
    public String contentPage(@PathVariable("id")Long id, Model model) {

        model.addAttribute("Content", imageFileService.getImage(id));


        return "content";
    }

    @GetMapping("/delete/{id}")
    public String deleteImage(@PathVariable("id")Long id) {

        imageFileService.deleteImage(id);

        return "editor";
    }
    @GetMapping("/editor/{id}")
    public String updatePage(@PathVariable("id")Long id, Model model) {

        model.addAttribute("data", imageFileService.getImage(id));

        return "editor";
    }

    @PostMapping("/save/{id}")
    public String updateLogic(ImageCreateReq imageCreateReq, @PathVariable("id")Long id) {

        imageFileService.updateOneContent(imageCreateReq, id);

        return "redirect:/api/v1/image/" + id;
    }
}