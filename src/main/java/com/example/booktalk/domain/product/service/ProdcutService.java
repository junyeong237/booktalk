package com.example.booktalk.domain.product.service;

import com.example.booktalk.domain.product.dto.request.ProductRegisterReq;
import com.example.booktalk.domain.product.dto.request.ProductUpdateReq;
import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;
import com.example.booktalk.domain.product.dto.response.ProductGetRes;
import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.product.dto.response.ProductRegisterRes;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.NotPermissionAuthority;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdcutService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductRegisterRes registerProduct(Long userId, ProductRegisterReq req) {

        User user = findUser(userId);

        Product product = Product.builder()
            .name(req.name())
            .price(req.price())
            .quantity(req.quantity())
            .regions(req.regions())
            .user(user)
            .build();

        productRepository.save(product);

        return new ProductRegisterRes(product.getId(), product.getName(), product.getQuantity()
            , product.getRegions(), product.getFinished(), user); // prodcut.getUser()와 user의 성능차이

    }

    public ProductRegisterRes updateRegister(Long userId, Long productId, ProductUpdateReq req) {

        User user = findUser(userId);
        Product product = findProduct(productId);
        validateProductUser(user, product);

        product.update(req);

        return new ProductRegisterRes(product.getId(), product.getName(),
            product.getQuantity(), product.getRegions(),
            product.getFinished(), user);

    }


    public ProductGetRes getProduct(Long productId) {

        Product product = findProduct(productId);

        return new ProductGetRes(product.getId(), product.getName(), product.getPrice()
            , product.getQuantity(), product.getRegions(), product.getFinished());

    }

    public List<ProductListRes> getProductList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.findAll(sort);

        return productList.stream()
            .map(
                product -> new ProductListRes(product.getId(), product.getName()
                    , product.getPrice(), product.getQuantity(), product.getRegions())
            )
            .toList();

    }

//    public List<ProductSerachListRes> getProductSearchList(String sortBy, Boolean isAsc,
//        String search) {
//
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//
//        List<Product> productList = productRepository.findAllBySearch(search, sort);
//
//    }


    public ProductDeleteRes deleteProduct(Long userId, Long productId) {
        User user = findUser(userId);
        Product product = findProduct(productId);
        validateProductUser(user, product);

        productRepository.delete(product);

        return new ProductDeleteRes("삭제가 완료되었습니다.");

    }


    private User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당하는유저가 없습니다."));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }

    private void validateProductUser(User user, Product product) {

        if (!user.getId.equals(product.getUser().getId())) {
            throw new NotPermissionAuthority(ProductErrorCode.NOT_PERMISSION_AUTHORITHY);
        }
    }

}
