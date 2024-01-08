package com.example.booktalk.domain.category.service;

import com.example.booktalk.domain.category.dto.response.CategoryListRes;
import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<CategoryListRes> getCategoryList() {

        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream()
            .map(
                category ->
                    new CategoryListRes(category.getName())
            ).toList();

    }


}
