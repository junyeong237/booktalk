package com.example.booktalk.domain.imageFile.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.example.booktalk.domain.imageFile.dto.request.ImageCreateReq;
import com.example.booktalk.domain.imageFile.entity.ImageFile;
import com.example.booktalk.domain.imageFile.repository.ImageFileRepository;
import com.example.booktalk.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageFileService {

    private final S3Config s3Config;

    private final ImageFileRepository imageFileRepository;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String localLocation = "C:\\Users\\rjsdn\\OneDrive\\Desktop\\vs\\";

    public String imageUpload(MultipartRequest request) throws IOException {

        MultipartFile file = request.getFile("upload");

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = localLocation + uuidFileName;

        File localFile = new File(localPath);
        file.transferTo(localFile);


        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        localFile.delete();

        return s3Url;

    }

    public void createImage(ImageCreateReq imageCreateReq) {

        ImageFile imageFile= ImageFile.builder()
                        .image(imageCreateReq.getContent()).build();

        imageFileRepository.save(imageFile);

    }

    public List<ImageFile> getImages() {

        return imageFileRepository.findAll();
    }

    public ImageFile getImage(Long id) {


        return imageFileRepository.findById(id).orElseThrow();
    }

    public void deleteImage(Long id) {

        imageFileRepository.deleteById(id);
    }

    public void updateOneContent(ImageCreateReq imageCreateReq, Long id) {


        ImageFile imageFile = ImageFile.builder()
                .id(id)
                .image(imageCreateReq.getContent())
                .build();

        imageFileRepository.save(imageFile);
    }
}