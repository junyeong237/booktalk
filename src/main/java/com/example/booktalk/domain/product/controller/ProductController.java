package com.example.booktalk.domain.product.controller;


import com.example.booktalk.domain.product.dto.request.ProductCreateReq;
import com.example.booktalk.domain.product.dto.request.ProductUpdateReq;
import com.example.booktalk.domain.product.dto.response.ProductCreateRes;
import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;
import com.example.booktalk.domain.product.dto.response.ProductGetRes;
import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.product.dto.response.ProductSerachListRes;
import com.example.booktalk.domain.product.dto.response.ProductTagListRes;
import com.example.booktalk.domain.product.dto.response.ProductUpdateRes;
import com.example.booktalk.domain.product.service.ProductService;
import com.example.booktalk.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductCreateRes createProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody ProductCreateReq req
    ) {
        return productService.createProduct(userDetails.getUser().getId(), req);

    }

    @PatchMapping("/{productId}")
    public ProductUpdateRes updateProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long productId,
        @RequestBody ProductUpdateReq req
    ) {
        return productService.updateProduct(userDetails.getUser().getId(), productId, req);
    }

    @DeleteMapping("/{productId}")
    public ProductDeleteRes deleteProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long productId
    ) {
        return productService.deleteProduct(userDetails.getUser().getId(), productId);
    }

    @GetMapping("/{productId}") //단일 조회
    public ProductGetRes getProduct(
        @PathVariable Long productId
    ) {
        return productService.getProduct(productId);
    }


    @GetMapping //상품 리스트 조회
    public List<ProductListRes> getProductList(
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
    ) {
        return productService.getProductList(sortBy, isAsc);
    }

    @GetMapping("/search") //상품 검색 리스트 조회
    public List<ProductSerachListRes> getProductSearchList(
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
        @RequestParam(value = "query") String search
    ) {
        return productService.getProductSearchList(sortBy, isAsc, search);
    }

    @GetMapping("/tag") //상품 검색 리스트 조회
    public List<ProductTagListRes> getProductListByTag(
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
        @RequestParam(value = "tag") String search
    ) {
        return productService.getProductSearchTagList(sortBy, isAsc, search);
    }

}
