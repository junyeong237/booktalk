package com.example.booktalk.domain.imageFile.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.booktalk.domain.imageFile.dto.response.ImageCreateRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageDeleteRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageListRes;
import com.example.booktalk.domain.imageFile.entity.ImageFile;
import com.example.booktalk.domain.imageFile.repository.ImageFileRepository;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotPermissionAuthority;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.config.S3Config;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageFileService {

    private final UserRepository userRepository;
    private final S3Config s3Config;
    private final ImageFileRepository imageFileRepository;
    private final ProductRepository productRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<ImageCreateRes> createImage(Long userId, Long productId, List<MultipartFile> files)
        throws IOException {
        List<ImageCreateRes> imageCreateResList = new ArrayList<>();

        for (MultipartFile file : files) {
            String imagePathUrl = imageUpload(file);
            User user = userRepository.findUserByIdWithThrow(userId);
            Product product = productRepository.findProductByIdWithThrow(productId);
            ImageFile imageFile = ImageFile.builder()
                .imagePathUrl(imagePathUrl)
                .user(user)
                .product(product)
                .build();
            imageFileRepository.save(imageFile);
            ImageCreateRes imageResponse = new ImageCreateRes(imageFile.getImagePathUrl());
            imageCreateResList.add(imageResponse);
        }
        return imageCreateResList;
    }

    @Transactional(readOnly = true)
    public List<ImageListRes> getImages(Long productId) {
        List<ImageFile> imageList = imageFileRepository.findByProductId(productId);
        return imageList.stream()
            .map(imageFile -> new ImageListRes(imageFile.getImagePathUrl()))
            .toList();
    }

    public List<ImageCreateRes> updateImage(Long userId, Long productId, List<MultipartFile> files)
        throws IOException {
        deleteImage(userId, productId);
        return createImage(userId, productId, files);
    }

    public ImageDeleteRes deleteImage(Long userId, Long productId) {
        User user = userRepository.findUserByIdWithThrow(userId);
        List<ImageFile> imageFileList = imageFileRepository.findByProductId(productId);
        for (ImageFile imageFile : imageFileList) {
            validateProductUser(user, imageFile);
            imageFileRepository.delete(imageFile);
        }
        return new ImageDeleteRes("삭제가 완료되었습니다.");
    }

    public String imageUpload(@RequestParam("upload") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        String uuidFileName = UUID.randomUUID() + ext;

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            s3Config.amazonS3Client().putObject(
                new PutObjectRequest(bucket, uuidFileName, byteArrayInputStream,
                    metadata).withCannedAcl(
                    CannedAccessControlList.PublicRead));
        }

        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();
        return s3Url;
    }


    private void validateProductUser(User user, ImageFile imageFile) {
        if (!user.getId().equals(imageFile.getUser().getId())) {
            throw new NotPermissionAuthority(ProductErrorCode.NOT_PERMISSION_AUTHORITHY);
        }
    }
}