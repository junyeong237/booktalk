package com.example.booktalk.domain.productLike.service;

import com.example.booktalk.domain.imageFile.dto.response.ImageListRes;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.productLike.dto.response.ProductLikeSwitchRes;
import com.example.booktalk.domain.productLike.entity.ProductLike;
import com.example.booktalk.domain.productLike.exception.NotPermissionMineException;
import com.example.booktalk.domain.productLike.exception.ProductLikeErrorCode;
import com.example.booktalk.domain.productLike.repository.ProductLikeRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductLikeService {

    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final UserRepository userRepository;
    private final ImageFileService imageFileService;

    public ProductLikeSwitchRes switchProductLike(Long productId, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);

        Product product = productRepository.findProductByIdWithThrow(productId);

        ProductLike productLike = productLikeRepository.findByProductAndUser(product, user)
            .orElseGet(() -> saveProductLike(product, user));

        Boolean updated = productLike.clickProdctLike();
        product.updateProductLikeCnt(updated);

        return new ProductLikeSwitchRes(productLike.getIsProductLiked());
    }


    public ProductLike saveProductLike(Product product, User user) {
        if (user.getId().equals(product.getUser().getId())) {
            throw new NotPermissionMineException(ProductLikeErrorCode.NOT_PERMISSION_MINE);
        }

        ProductLike productLike = ProductLike.builder()
            .product(product)
            .user(user)
            .build();

        return productLikeRepository.save(productLike);
    }

    @Transactional(readOnly = true)
    public List<ProductListRes> getMyLikedProducts(Long userId) {
        List<ProductLike> productLikeList = productLikeRepository.findByUser_IdAndIsProductLikedTrue(
            userId);
        List<Product> likedProducts = new ArrayList<>();

        for (ProductLike productLike : productLikeList) {
            Product product = productRepository.findProductByIdWithThrow(
                productLike.getProduct().getId());
            if (product.getDeleted()) {
                continue;
            }
            likedProducts.add(product);
        }

        return likedProducts.stream()
            .map(product -> {
                List<ImageListRes> imageListRes = imageFileService.getImages(product.getId());

                List<String> categories = product.getProductCategoryList().stream()
                    .map(productCategory -> {
                        return productCategory.getCategory().getName();
                    })
                    .toList();
                ImageListRes imageGetRes = imageListRes.isEmpty() ? null : imageListRes.get(0);

                return new ProductListRes(product.getId(), product.getName(), product.getPrice(),
                    product.getQuantity(), product.getProductLikeCnt(), categories,
                    product.getRegion(), imageGetRes);
            })
            .toList();
    }


}
