package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.admin.service.AdminCategoryService;
import com.example.booktalk.domain.category.dto.request.CategoryCreateReq;
import com.example.booktalk.domain.category.dto.request.CategoryUpdateReq;
import com.example.booktalk.domain.category.dto.response.CategoryCreateRes;
import com.example.booktalk.domain.category.dto.response.CategoryDeleteRes;
import com.example.booktalk.domain.category.dto.response.CategoryUpdateRes;
import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.exception.ForbiddenDeleteCategoryException;
import com.example.booktalk.domain.category.repository.CategoryRepository;
import com.example.booktalk.domain.productcategory.repository.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class AdminCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private AdminCategoryService adminCategoryService;

    private Category existingCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        existingCategory = Category.builder().build();
        ReflectionTestUtils.setField(existingCategory, "id", 1L);
        ReflectionTestUtils.setField(existingCategory, "name", "OldCategory");
    }

    @Test
    @DisplayName("카테고리 생성 테스트")
    void 카테고리_생성_테스트() {
        // Given
        CategoryCreateReq req = new CategoryCreateReq("TestCategory");
        given(categoryRepository.existsByName(any())).willReturn(false);
        given(categoryRepository.save(any(Category.class))).willAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedCategory, "id", 1L);
            return savedCategory;
        });

        // When
        CategoryCreateRes result = adminCategoryService.createCategory(req);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("TestCategory", result.name());
        verify(categoryRepository, times(1)).existsByName("TestCategory");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("카테고리 업데이트 테스트")
    void 카테고리_업데이트_테스트() {
        // Given
        Long categoryId = 1L;
        CategoryUpdateReq req = new CategoryUpdateReq("UpdatedCategory");
        given(categoryRepository.findCategoryByIdWithThrow(categoryId)).willReturn(existingCategory);
        given(categoryRepository.existsByName(any())).willReturn(false);

        // When
        CategoryUpdateRes result = adminCategoryService.updateCategory(categoryId, req);

        // Then
        assertNotNull(result);
        assertEquals(categoryId, result.id());
        assertEquals("UpdatedCategory", result.name());
        verify(categoryRepository, times(1)).findCategoryByIdWithThrow(categoryId);
        verify(categoryRepository, times(1)).existsByName("UpdatedCategory");
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void 카테고리_삭제_테스트() {
        // Given
        Long categoryId = 1L;
        given(categoryRepository.findCategoryByIdWithThrow(categoryId)).willReturn(existingCategory);
        given(productCategoryRepository.existsByCategory_Id(categoryId)).willReturn(false);

        // When
        CategoryDeleteRes result = adminCategoryService.deleteCategory(categoryId);

        //Then
        assertNotNull(result);
        assertEquals("카테고리가 삭제가 완료되었습니다.", result.message());
        verify(categoryRepository, times(1)).findCategoryByIdWithThrow(categoryId);
        verify(productCategoryRepository, times(1)).existsByCategory_Id(categoryId);
        verify(categoryRepository, times(1)).delete(existingCategory);
    }

    @Test
    @DisplayName("카테고리 삭제 테스트 (금지된 예외 발생)")
    void 카테고리_삭제_테스트_금지된_예외_발생() {
        Long categoryId = 1L;
        given(categoryRepository.findCategoryByIdWithThrow(categoryId)).willReturn(existingCategory);
        given(productCategoryRepository.existsByCategory_Id(categoryId)).willReturn(true);

        assertThrows(ForbiddenDeleteCategoryException.class, () -> adminCategoryService.deleteCategory(categoryId));
        verify(categoryRepository, times(1)).findCategoryByIdWithThrow(categoryId);
        verify(productCategoryRepository, times(1)).existsByCategory_Id(categoryId);
        verify(categoryRepository, never()).delete(existingCategory);
    }
}
