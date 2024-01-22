package com.example.booktalk.domain.category.controller;


import com.example.booktalk.domain.category.dto.response.CategoryListRes;
import com.example.booktalk.domain.category.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryListRes>> getCategoryList() {

        List<CategoryListRes> categoryListRes = categoryService.getCategoryList();

        return ResponseEntity.status(HttpStatus.OK)
            .body(categoryListRes);


    }

}
