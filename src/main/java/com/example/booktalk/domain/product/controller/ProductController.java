package com.example.booktalk.domain.product.controller;


import com.example.booktalk.domain.product.dto.request.ProductCreateReq;
import com.example.booktalk.domain.product.dto.request.ProductUpdateReq;
import com.example.booktalk.domain.product.dto.response.ProductCreateRes;
import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;
import com.example.booktalk.domain.product.dto.response.ProductGetRes;
import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.product.dto.response.ProductSerachListRes;
import com.example.booktalk.domain.product.dto.response.ProductTagListRes;
import com.example.booktalk.domain.product.dto.response.ProductTopLikesListRes;
import com.example.booktalk.domain.product.dto.response.ProductUpdateRes;
import com.example.booktalk.domain.product.service.ProductService;
import com.example.booktalk.global.security.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductCreateRes> createProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestPart("req") ProductCreateReq req,
        @RequestParam("upload") List<MultipartFile> files
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.createProduct(userDetails.getUser().getId(), req, files));

    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductUpdateRes> updateProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long productId,
        @RequestPart("req") ProductUpdateReq req,
        @RequestParam("upload") List<MultipartFile> files
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                productService.updateProduct(userDetails.getUser().getId(), productId, req, files));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDeleteRes> deleteProduct(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long productId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.deleteProduct(userDetails.getUser().getId(), productId));
    }

    @GetMapping("/{productId}") //단일 조회
    public ResponseEntity<ProductGetRes> getProduct(
        @PathVariable Long productId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.getProduct(productId));
    }


    @GetMapping //상품 리스트 조회
    public ResponseEntity<Page<ProductListRes>> getProductList(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.getProductList(page - 1, size, sortBy, isAsc));
    }

    @GetMapping("/main") //메인화면에 관심상품 Top3 인 product 출력
    public ResponseEntity<List<ProductTopLikesListRes>> getProductListTopThree() {

        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.getProductListByLikesTopThree());
    }

    @GetMapping("/search") //상품 검색 리스트 조회
    public ResponseEntity<Page<ProductSerachListRes>> getProductSearchList(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
        @RequestParam(value = "query") String search
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.getProductSearchList(page - 1, size, sortBy, isAsc, search));
    }

    @GetMapping("/tag") //상품 검색 리스트 조회
    public ResponseEntity<Page<ProductTagListRes>> getProductListByTag(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
        @RequestParam(value = "tag") String search
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.getProductSearchTagList(page - 1, size, sortBy, isAsc, search));
    }

}
