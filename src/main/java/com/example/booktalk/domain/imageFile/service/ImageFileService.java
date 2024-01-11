package com.example.booktalk.domain.imageFile.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.booktalk.domain.imageFile.dto.response.*;
import com.example.booktalk.domain.imageFile.entity.ImageFile;
import com.example.booktalk.domain.imageFile.exception.ImageFileErrorCode;
import com.example.booktalk.domain.imageFile.exception.NotFoundImageFileException;
import com.example.booktalk.domain.imageFile.repository.ImageFileRepository;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.NotPermissionAuthority;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.exception.NotFoundUserException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    private String localLocation = "C:\\Users\\rjsdn\\OneDrive\\Desktop\\vs\\";



    public ImageCreateRes createImage(Long userId, Long productId, @RequestParam("upload") MultipartFile file) throws IOException {
        String image=imageUpload(file);
        User user=findUser(userId);
        Product product=findProduct(productId);
        ImageFile imageFile= ImageFile.builder()
                        .image(image)
                        .user(user)
                        .product(product)
                        .build();
        imageFileRepository.save(imageFile);
        return new ImageCreateRes(imageFile.getId(), imageFile.getImage());
    }
    @Transactional(readOnly = true)
    public ImageGetRes getImage(Long productId,Long imageId) {

        ImageFile imageFile=findImage(productId,imageId);

        return new ImageGetRes(imageFile.getId(), imageFile.getImage());

    }


    @Transactional(readOnly = true)
    public List<ImageListRes> getImages(Long productId) {

        List<ImageFile> imageList=imageFileRepository.findByProductId(productId);

        return imageList.stream()
                .map(
                        imageFile -> new ImageListRes(imageFile.getId(), imageFile.getImage())
                )
                .toList();
    }
    @Transactional
    public ImageUpdateRes updateImage(Long userId,Long productId, Long imageId, @RequestParam("upload") MultipartFile file) throws IOException {
        User user = findUser(userId);
        String image = imageUpload(file);
        ImageFile imageFile = findImage(productId,imageId);
        validateProductUser(user, imageFile);
        imageFile.updateImage(image);
        return new ImageUpdateRes(imageFile.getId(),imageFile.getImage());
    }
    public ImageDeleteRes deleteImage(Long userId,Long productId, Long imageId) {
        User user = findUser(userId);
        ImageFile imageFile = findImage(productId,imageId);
        validateProductUser(user, imageFile);
        imageFileRepository.delete(imageFile);

        return new ImageDeleteRes("삭제가 완료되었습니다.");
    }
    public String imageUpload(@RequestParam("upload") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));

        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = localLocation + uuidFileName;

        File localFile = new File(localPath);
        file.transferTo(localFile);

        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        localFile.delete();

        return s3Url;
    }
    private ImageFile findImage(Long productId,Long imageId) {
        return imageFileRepository.findByProductIdAndId(productId,imageId)
                .orElseThrow(() -> new NotFoundImageFileException(ImageFileErrorCode.NOT_FOUND_IMAGE));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException(UserErrorCode.NOT_FOUND_USER));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }

    private void validateProductUser(User user, ImageFile imageFile) {

        if (!user.getId().equals(imageFile.getUser().getId())) {
            throw new NotPermissionAuthority(ProductErrorCode.NOT_PERMISSION_AUTHORITHY);
        }
    }

}