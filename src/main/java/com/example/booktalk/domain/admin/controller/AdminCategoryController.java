package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminCategoryService;
import com.example.booktalk.domain.category.dto.request.CategoryCreateReq;
import com.example.booktalk.domain.category.dto.request.CategoryUpdateReq;
import com.example.booktalk.domain.category.dto.response.CategoryCreateRes;
import com.example.booktalk.domain.category.dto.response.CategoryDeleteRes;
import com.example.booktalk.domain.category.dto.response.CategoryUpdateRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<CategoryCreateRes> createCategory(
        @RequestBody CategoryCreateReq req
    ) {
        CategoryCreateRes res = adminCategoryService.createCategory(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryUpdateRes> updateCategory(
        @PathVariable Long categoryId,
        @RequestBody CategoryUpdateReq req
    ) {
        CategoryUpdateRes res = adminCategoryService.updateCategory(categoryId, req);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CategoryDeleteRes> deleteCategory(
        @PathVariable Long categoryId
    ) {
        CategoryDeleteRes res = adminCategoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
