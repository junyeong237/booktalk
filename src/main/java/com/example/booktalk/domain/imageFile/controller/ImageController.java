package com.example.booktalk.domain.imageFile.controller;


import com.example.booktalk.domain.imageFile.dto.response.*;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products/{productId}/image")
public class ImageController {

    private ImageFileService imageFileService;



    @PostMapping("/save")
    @ResponseBody
    public ImageCreateRes createImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long productId,
                                      @RequestParam("upload") MultipartFile file) throws IOException {

        return imageFileService.createImage(userDetails.getUser().getId(),productId,file);
    }
    @GetMapping("/{imageId}") //단일 조회
    public ImageGetRes getImage(@PathVariable Long productId,
            @PathVariable Long imageId) {
        return imageFileService.getImage(productId,imageId);
    }
    @GetMapping//다건 조희
    public List<ImageListRes> getImages(@PathVariable Long productId) {
         return imageFileService.getImages(productId);
    }
    @PutMapping("/{imageId}") //이미지 수정
    public ImageUpdateRes updateImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long productId,
                                      @PathVariable Long imageId,
                                      @RequestParam("upload") MultipartFile file) throws IOException {
        return imageFileService.updateImage(userDetails.getUser().getId(), productId,imageId,file);
    }
    @DeleteMapping("/{imageId}") //이미지 삭제
    public ImageDeleteRes deleteImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long productId,
                                      @PathVariable Long imageId) {
        return imageFileService.deleteImage(userDetails.getUser().getId(), productId,imageId);
    }


}