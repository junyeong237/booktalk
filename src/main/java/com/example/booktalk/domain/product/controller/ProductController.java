package com.example.booktalk.domain.product.controller;


import com.example.booktalk.domain.product.dto.request.ProductCreateReq;
import com.example.booktalk.domain.product.dto.request.ProductUpdateReq;
import com.example.booktalk.domain.product.dto.response.*;
import com.example.booktalk.domain.product.service.ProductService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductCreateRes createProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestPart("req") ProductCreateReq req,
        @RequestParam("upload") List<MultipartFile> files
    ) throws IOException {
        return productService.createProduct(userDetails.getUser().getId(), req, files);

    }

    @PutMapping("/{productId}")
    public ProductUpdateRes updateProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long productId,
        @RequestPart("req") ProductUpdateReq req,
        @RequestParam("upload") List<MultipartFile> files
    ) throws IOException {
        return productService.updateProduct(userDetails.getUser().getId(), productId, req, files);
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
    public Page<ProductListRes> getProductList(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
    ) {
        return productService.getProductList(page - 1, size, sortBy, isAsc);
    }

    @GetMapping("/main") //메인화면에 관심상품 Top3 인 product 출력
    public List<ProductTopLikesListRes> getProductListTopThree() {

        return productService.getProductListByLikesTopThree();
    }

    @GetMapping("/search") //상품 검색 리스트 조회
    public Page<ProductSerachListRes> getProductSearchList(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
        @RequestParam(value = "query") String search
    ) {
        return productService.getProductSearchList(page - 1, size, sortBy, isAsc, search);
    }

    @GetMapping("/tag") //상품 검색 리스트 조회
    public Page<ProductTagListRes> getProductListByTag(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
        @RequestParam(value = "tag") String search
    ) {
        return productService.getProductSearchTagList(page - 1, size, sortBy, isAsc, search);
    }

}
