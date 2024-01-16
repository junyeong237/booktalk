package com.example.booktalk.domain.imageFile.controller;


import com.example.booktalk.domain.imageFile.dto.request.CreateImageReq;
import com.example.booktalk.domain.imageFile.dto.request.DeleteImageReq;
import com.example.booktalk.domain.imageFile.dto.request.GetImageReq;
import com.example.booktalk.domain.imageFile.dto.request.UpdateImageReq;
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
@RequestMapping("/api/v1/image")
public class ImageController {

    private ImageFileService imageFileService;



    @PostMapping
    @ResponseBody
    public List<ImageCreateRes> createImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestPart("productId") CreateImageReq req,
                                      @RequestParam("upload") List<MultipartFile> files) throws IOException {

        return imageFileService.createImage(userDetails.getUser().getId(),req.productId(),files);
    }
    @GetMapping("/{imageId}") //단일 조회
    public ImageGetRes getImage(@RequestBody GetImageReq req,
                                @PathVariable Long imageId) {
        return imageFileService.getImage(req.productId(),imageId);
    }
    @GetMapping//다건 조희
    public List<ImageListRes> getImages(@RequestBody GetImageReq req) {
         return imageFileService.getImages(req.productId());
    }
    @PutMapping("/{imageId}") //이미지 수정
    public ImageUpdateRes updateImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestPart("productId") CreateImageReq req,
                                      @PathVariable Long imageId,
                                      @RequestParam("upload") MultipartFile file) throws IOException {
        return imageFileService.updateImage(userDetails.getUser().getId(), req.productId(),imageId,file);
    }
    @DeleteMapping("/{imageId}") //이미지 삭제
    public ImageDeleteRes deleteImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody DeleteImageReq req,
                                      @PathVariable Long imageId) {
        return imageFileService.deleteImage(userDetails.getUser().getId(), req.productId(),imageId);
    }


}