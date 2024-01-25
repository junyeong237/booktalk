package com.example.booktalk.domain.imageFile.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.booktalk.domain.imageFile.dto.response.ImageCreateRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageDeleteRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageListRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageUpdateRes;
import com.example.booktalk.domain.imageFile.entity.ImageFile;
import com.example.booktalk.domain.imageFile.repository.ImageFileRepository;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.config.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ImageFileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageFileRepository imageFileRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private S3Config s3Config;

    @Mock
    private AmazonS3Client s3ClientMock;

    @InjectMocks
    private ImageFileService imageFileService;

    private MockMultipartFile file;
    private MockMultipartFile file1;
    private MockMultipartFile file2;
    private User user;
    private Product product;
    private ImageFile imageFile;

    private  List<MultipartFile> fileList = new ArrayList<>();

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        when(s3Config.amazonS3Client()).thenReturn(s3ClientMock);


        file = ImageFileTestUtils.createMockImageFile("test-image.jpg", "jpg", 800, 600);
        file1 = ImageFileTestUtils.createMockImageFile("1test-image.jpg", "jpg", 1024, 768);
        file2 = ImageFileTestUtils.createMockImageFile("2test-image.jpg", "jpg", 640, 480);
        fileList.add(file);
        fileList.add(file1);
        fileList.add(file2);

        user = User.builder().build();
        ReflectionTestUtils.setField(user, "id", 1L);

        product = Product.builder().build();
        ReflectionTestUtils.setField(product, "id", 1L);

        imageFile = new ImageFile();
        ReflectionTestUtils.setField(imageFile, "id", 1L);
        ReflectionTestUtils.setField(imageFile, "user", user);
    }
    @Test
    @DisplayName("이미지 생성 테스트")
    public void testCreateImage() throws IOException {
        // Mock 데이터 설정
        Long userId = 1L;
        Long productId = 1L;

        // userRepository, productRepository 등의 의존성이 있는 객체들을 Mock으로 설정
        when(userRepository.findUserByIdWithThrow(userId)).thenReturn(user);

        when(productRepository.findProductByIdWithThrow(productId)).thenReturn(product);

        when(imageFileRepository.save(any(ImageFile.class))).thenAnswer(invocation -> {
            ImageFile savedImageFile = invocation.getArgument(0);// 이미지 파일에 자동으로 부여되는 ID 등 설정
            ReflectionTestUtils.setField(imageFile, "id", 1L);
            return savedImageFile;
        });
        when(s3Config.amazonS3Client()).thenReturn(s3ClientMock);

        when(s3ClientMock.getUrl(anyString(), anyString())).thenReturn(new URL("https://example.com/test-image.jpg"));

        ReflectionTestUtils.setField(imageFileService, "bucket", "test");

        // 테스트 대상 메서드 호출
        List<ImageCreateRes> result = imageFileService.createImage(userId, productId, fileList);

        // 결과 확인
        assertEquals(fileList.size(), result.size());

        // userRepository, productRepository 등이 제대로 호출되었는지 확인
        verify(userRepository, times(3)).findUserByIdWithThrow(userId);
        verify(productRepository, times(3)).findProductByIdWithThrow(productId);

        // imageFileRepository.save()가 files.size()번 호출되었는지 확인
        verify(imageFileRepository, times(fileList.size())).save(any(ImageFile.class));
    }


    @Test
    @DisplayName("이미지 목록 조회 테스트")
    public void testGetImages() {
        // Arrange
        Long productId = 1L;

        List<ImageFile> mockImageList = Stream.of(new ImageFile(), new ImageFile())
                .collect(Collectors.toList());
        when(imageFileRepository.findByProductId(productId)).thenReturn(mockImageList);

        // Act
        List<ImageListRes> result = imageFileService.getImages(productId);

        // Assert
        Mockito.verify(imageFileRepository, Mockito.times(1)).findByProductId(productId);
        assertEquals(mockImageList.size(), result.size());
    }
    @Test
    @DisplayName("이미지 수정 테스트")
    public void testUpdateImage() throws IOException {
        Long userId = 1L; // 유저 ID
        Long productId = 1L; // 상품 ID


        when(s3Config.amazonS3Client()).thenReturn(s3ClientMock);

        when(s3ClientMock.getUrl(anyString(), anyString())).thenReturn(new URL("https://example.com/test-image.jpg"));

        ReflectionTestUtils.setField(imageFileService, "bucket", "test");
        // 이미지 업데이트 메서드 호출
        List<ImageCreateRes> result = imageFileService.updateImage(userId, productId, fileList);

        // 이미지 업데이트 후의 결과 검증
        assertEquals(3, result.size()); // 예상되는 이미지 개수와 일치하는지 확인

        // (Optional) 실제 업데이트된 이미지 URL 등을 확인할 수 있습니다.
        // 여기서는 간단한 예시로 첫 번째 이미지만 확인
        assertEquals("https://example.com/test-image.jpg", result.get(0).imagePathUrl());

        // 이미지 업데이트 전의 이미지가 삭제되었는지 확인
        List<ImageFile> deletedImages = imageFileRepository.findByProductId(productId);
        assertEquals(0, deletedImages.size()); // 이미지가 삭제되었으므로 크기는 0이어야 합니다.
    }

    // 이미지 삭제 테스트
    @Test
    @DisplayName("이미지 삭제 테스트")
    public void testDeleteImage() {
        Long userId = 1L; // 유저 ID
        Long productId = 1L; // 상품 ID

        // 이미지 삭제 메서드 호출
        imageFileService.deleteImage(userId, productId);

        // 이미지가 삭제되었는지 확인
        List<ImageFile> deletedImages = imageFileRepository.findByProductId(productId);
        assertEquals(0, deletedImages.size()); // 이미지가 삭제되었으므로 크기는 0이어야 합니다.
    }

    @Test
    @DisplayName("이미지 업로드 및 리사이징 테스트")
    public void testImageUpload() throws IOException {
        // Mock 데이터 설정
        String expectedS3Url = "https://example.com/test-image.jpg";

        when(s3Config.amazonS3Client()).thenReturn(s3ClientMock);

        when(s3ClientMock.getUrl(anyString(), anyString())).thenReturn(new URL(expectedS3Url));

        ReflectionTestUtils.setField(imageFileService, "bucket", "test");

        // 이미지 업로드 메서드 호출
        String actualS3Url = imageFileService.imageUpload(file);

        // 결과 확인
        assertEquals(expectedS3Url, actualS3Url);

        // S3에 업로드 메서드가 1번 호출되었는지 확인
        verify(s3ClientMock, times(1)).putObject(any(PutObjectRequest.class));

        // S3 URL을 가져오는 메서드가 1번 호출되었는지 확인
        verify(s3ClientMock, times(1)).getUrl(eq("test"), anyString());
    }

    @Test
    public void testResizer() throws IOException {

        // 리사이징된 이미지 획득
        MultipartFile resizedImage = imageFileService.resizer(file);

        BufferedImage image = ImageIO.read(file.getInputStream());

        // 리사이징 확인 (가로 크기가 500 이하이면 리사이징이 이루어지지 않는 것으로 가정)
        if (image.getWidth() > 500) {
            assertEquals(500, ImageIO.read(resizedImage.getInputStream()).getWidth());
        } else {
            // 리사이징이 이루어지지 않았다면, 원본 이미지와 리사이징된 이미지의 크기는 같아야 함
            assertEquals(image.getWidth(), ImageIO.read(resizedImage.getInputStream()).getWidth());
        }

        // JPEG 변환 확인
        assertEquals("image/jpg", resizedImage.getContentType());

    }

}
