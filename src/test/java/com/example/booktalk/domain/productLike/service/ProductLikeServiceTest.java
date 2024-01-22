package com.example.booktalk.domain.productLike.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.productLike.dto.response.ProductLikeSwitchRes;
import com.example.booktalk.domain.productLike.entity.ProductLike;
import com.example.booktalk.domain.productLike.repository.ProductLikeRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class ProductLikeServiceTest {

    @Mock
    private ProductLikeRepository productLikeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductLikeService productLikeService;

    private User user;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = User.builder().build();
        ReflectionTestUtils.setField(user, "id", 1L);

        product = mock(Product.class);
        ReflectionTestUtils.setField(product, "id", 1L);
    }

    @Test
    public void 상품좋아요스위치_상품좋아요존재시_상태test() {
        // Given
        when(userRepository.findUserByIdWithThrow(user.getId())).thenReturn(user);
        when(productRepository.findProductByIdWithThrow(product.getId())).thenReturn(product);

        ProductLike productLike = ProductLike.builder().product(product).user(user).build();
        when(productLikeRepository.findByProductAndUser(product, user)).thenReturn(
            Optional.of(productLike));

        // When
        ProductLikeSwitchRes result = productLikeService.switchProductLike(product.getId(),
            user.getId());

        // Then
        assertEquals(productLike.getIsProductLiked(), result.isProductLiked());

        // Verify
        verify(userRepository, times(1)).findUserByIdWithThrow(user.getId());
        verify(productRepository, times(1)).findProductByIdWithThrow(product.getId());
        verify(productLikeRepository, times(1)).findByProductAndUser(product, user);
        verify(productLikeRepository, times(0)).save(any(ProductLike.class));
        verify(product, times(1)).updateProductLikeCnt(any(Boolean.class));
    }

}
