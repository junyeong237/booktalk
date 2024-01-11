package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.category.dto.request.CategoryCreateReq;
import com.example.booktalk.domain.category.dto.request.CategoryUpdateReq;
import com.example.booktalk.domain.category.dto.response.CategoryCreateRes;
import com.example.booktalk.domain.category.dto.response.CategoryDeleteRes;
import com.example.booktalk.domain.category.dto.response.CategoryUpdateRes;
import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.exception.CategoryErrorCode;
import com.example.booktalk.domain.category.exception.ExistCategoryException;
import com.example.booktalk.domain.category.exception.ForbiddenDeleteCategoryException;
import com.example.booktalk.domain.category.repository.CategoryRepository;
import com.example.booktalk.domain.productcategory.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public CategoryCreateRes createCategory(CategoryCreateReq req) {

        validateDuplicateCategory(req.name());
        Category category = Category.builder()
            .name(req.name())
            .build();

        category = categoryRepository.save(category);

        return new CategoryCreateRes(category.getId(), category.getName());
    }


    public CategoryUpdateRes updateCategory(
        Long categoryId,
        CategoryUpdateReq req) {
        Category category = categoryRepository.findCategoryByIdWithThrow(categoryId);
        validateDuplicateCategory(req.name());
        category.update(req.name());
        return new CategoryUpdateRes(category.getId(), category.getName());
    }


    public CategoryDeleteRes deleteCategory(final Long categoryId) {
        Category category = categoryRepository.findCategoryByIdWithThrow(categoryId);

        if (productCategoryRepository.existsByCategory_Id(categoryId)) {
            throw new ForbiddenDeleteCategoryException(CategoryErrorCode.FORBIDDEM_DELETE_CATEGORY);
        }

        categoryRepository.delete(category);
        return new CategoryDeleteRes("카테고리가 삭제가 완료되었습니다.");
    }

    private void validateDuplicateCategory(String updateName) {

        if (categoryRepository.existsByName(updateName)) {
            throw new ExistCategoryException(CategoryErrorCode.EXIST_CATEGORY);
        }


    }
}
