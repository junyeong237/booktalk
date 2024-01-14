package com.example.booktalk.domain.imageFile.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.booktalk.domain.imageFile.dto.response.ImageDeleteRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageListRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageUpdateRes;
import com.example.booktalk.domain.imageFile.entity.ImageFile;
import com.example.booktalk.domain.imageFile.repository.ImageFileRepository;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private User user;
    private Product product;
    private ImageFile imageFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(s3Config.amazonS3Client()).thenReturn(s3ClientMock);

        file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        user = User.builder().build();
        ReflectionTestUtils.setField(user, "id", 1L);

        product = Product.builder().build();
        ReflectionTestUtils.setField(product, "id", 1L);

        imageFile = new ImageFile();
        ReflectionTestUtils.setField(imageFile, "id", 1L);
        ReflectionTestUtils.setField(imageFile, "user", user);
    }

    // 이미지 생성 테스트
    @Test
    @DisplayName("이미지 생성 테스트")
    public void 이미지_생성_테스트() throws IOException {
        // Given
        Long userId = 1L;
        Long productId = 1L;

        when(userRepository.findUserByIdWithThrow(anyLong())).thenReturn(user);
        when(productRepository.findProductByIdWithThrow(anyLong())).thenReturn(product);
        when(imageFileRepository.save(any())).thenReturn(imageFile);
        when(s3ClientMock.getUrl(anyString(), anyString())).thenReturn(new URL("https://test"));
        ReflectionTestUtils.setField(imageFileService, "bucket", "test");

        // When
        imageFileService.createImage(userId, productId, file);

        // Then
        verify(imageFileRepository, times(1)).save(any(ImageFile.class));
    }

    // 이미지 조회 테스트
    @Test
    @DisplayName("이미지 조회 테스트")
    public void 이미지_조회_테스트() {
        // Given
        Long productId = 1L;
        Long imageId = 1L;
        ImageFile mockImageFile = new ImageFile();
        when(imageFileRepository.findImageFileByProductIdAndIdWithThrow(anyLong(), anyLong())).thenReturn(mockImageFile);

        // When
        imageFileService.getImage(productId, imageId);

        // Then
        verify(imageFileRepository, times(1)).findImageFileByProductIdAndIdWithThrow(anyLong(), anyLong());
    }

    // 이미지 목록 조회 테스트
    @Test
    @DisplayName("이미지 목록 조회 테스트")
    public void 이미지_목록_조회_테스트() {
        // Given
        Long productId = 1L;
        List<ImageFile> mockImageList = Stream.of(new ImageFile(), new ImageFile()).collect(Collectors.toList());
        when(imageFileRepository.findByProductId(productId)).thenReturn(mockImageList);

        // When
        List<ImageListRes> result = imageFileService.getImages(productId);

        // Then
        verify(imageFileRepository, times(1)).findByProductId(productId);
        assertEquals(mockImageList.size(), result.size());
    }

    // 이미지 업데이트 테스트
    @Test
    @DisplayName("이미지 업데이트 테스트")
    public void 이미지_업데이트_테스트() throws IOException {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Long imageId = 1L;

        when(userRepository.findUserByIdWithThrow(anyLong())).thenReturn(user);
        when(imageFileRepository.findImageFileByProductIdAndIdWithThrow(anyLong(), anyLong())).thenReturn(imageFile);
        when(s3Config.amazonS3Client()).thenReturn(s3ClientMock);
        when(s3ClientMock.getUrl(anyString(), anyString())).thenReturn(new URL("https://example.com/test-image.jpg"));
        ReflectionTestUtils.setField(imageFileService, "bucket", "test");

        // When
        ImageUpdateRes result = imageFileService.updateImage(userId, productId, imageId, file);

        // Then
        assertNotNull(result);
        assertEquals("https://example.com/test-image.jpg", result.imagePathUrl());
    }

    // 이미지 삭제 테스트
    @Test
    @DisplayName("이미지 삭제 테스트")
    public void 이미지_삭제_테스트() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Long imageId = 1L;
        when(userRepository.findUserByIdWithThrow(userId)).thenReturn(user);
        when(imageFileRepository.findImageFileByProductIdAndIdWithThrow(productId, imageId)).thenReturn(imageFile);

        // When
        ImageDeleteRes result = imageFileService.deleteImage(userId, productId, imageId);

        // Then
        assertNotNull(result);
        assertEquals("삭제가 완료되었습니다.", result.message());
        verify(imageFileRepository, times(1)).delete(imageFile);
        verify(userRepository, times(1)).findUserByIdWithThrow(userId);
        verify(imageFileRepository, times(1)).findImageFileByProductIdAndIdWithThrow(productId, imageId);
    }


}
