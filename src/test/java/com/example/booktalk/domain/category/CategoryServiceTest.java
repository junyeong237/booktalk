package com.example.booktalk.domain.category;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.example.booktalk.domain.admin.service.AdminCategoryService;
import com.example.booktalk.domain.category.dto.request.CategoryCreateReq;
import com.example.booktalk.domain.category.dto.request.CategoryUpdateReq;
import com.example.booktalk.domain.category.dto.response.CategoryCreateRes;
import com.example.booktalk.domain.category.dto.response.CategoryDeleteRes;
import com.example.booktalk.domain.category.dto.response.CategoryListRes;
import com.example.booktalk.domain.category.dto.response.CategoryUpdateRes;
import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.exception.CategoryErrorCode;
import com.example.booktalk.domain.category.exception.NotFoundCategoryException;
import com.example.booktalk.domain.category.repository.CategoryRepository;
import com.example.booktalk.domain.category.service.CategoryService;
import com.example.booktalk.domain.productcategory.repository.ProductCategoryRepository;
import com.example.booktalk.global.exception.GlobalException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(MockitoExtension.class) // junit5용
@Transactional
public class CategoryServiceTest {


    @InjectMocks
    CategoryService categoryService;

    @InjectMocks
    AdminCategoryService adminCategoryService;
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ProductCategoryRepository productCategoryRepository;
    Long TEST_CATE_ID = 1L;
    String TEST_CATE_NAME = "추리";
    Long TEST_CATE2_ID = 2L;
    String TEST_CATE_NAME2 = "코미디";
    Long TEST_CATE3_ID = 3L;
    String TEST_CATE_NAME3 = "SF";
    Category TEST_CATE;
    Category TEST_ANOTHER_CATE;
    Category TEST_ANOTHER_CATE2;

    @BeforeEach
    void setup() {

        TEST_CATE = Category.builder()
            .name(TEST_CATE_NAME)
            .build();
        ReflectionTestUtils.setField(TEST_CATE, "id", TEST_CATE_ID);
        TEST_ANOTHER_CATE = Category.builder()
            .name(TEST_CATE_NAME2)
            .build();
        ReflectionTestUtils.setField(TEST_ANOTHER_CATE, "id", TEST_CATE2_ID);
        TEST_ANOTHER_CATE2 = Category.builder()
            .name(TEST_CATE_NAME3)
            .build();
        ReflectionTestUtils.setField(TEST_ANOTHER_CATE2, "id", TEST_CATE3_ID);
    }


    @Nested
    class 생성_테스트 {

        @DisplayName("카테고리 생성")
        @Test
        void 카테고리생성() {
            // given
            CategoryCreateReq req = new CategoryCreateReq(TEST_CATE_NAME);

            ReflectionTestUtils.setField(TEST_CATE, "id", TEST_CATE_ID);
            given(categoryRepository.save(any(Category.class))).willReturn(TEST_CATE);

            // when
            CategoryCreateRes res = adminCategoryService.createCategory(req);

            // then
            assertThat(res.id()).isEqualTo(TEST_CATE.getId());
            assertThat(res.name()).isEqualTo(TEST_CATE.getName());
            Mockito.verify(categoryRepository, Mockito.times(1)).save(any(Category.class));

        }
    }

    @Nested
    class 수정_테스트 {

        @DisplayName("카테고리 수정")
        @Test
        void 카테고리_수정() {
            // given
            CategoryUpdateReq req = new CategoryUpdateReq(TEST_CATE_NAME2);
            ReflectionTestUtils.setField(TEST_CATE, "id", TEST_CATE_ID);
            given(categoryRepository.findCategoryByIdWithThrow(eq(TEST_CATE_ID))).willReturn(
                TEST_CATE);

            // when
            CategoryUpdateRes res = adminCategoryService.updateCategory(TEST_CATE_ID, req);

            // then
            assertThat(res.id()).isEqualTo(TEST_CATE.getId());
            assertThat(req.name()).isEqualTo(res.name());
            assertThat(res.name()).isEqualTo(TEST_CATE.getName());
        }

        @DisplayName("카테고리 수정시 존재하지않는 카테고리")
        @Test
        void 카테고리_수정_존재하지않은_카테고리() {
            // given
            CategoryUpdateReq req = new CategoryUpdateReq(TEST_CATE_NAME2);
            given(categoryRepository.findCategoryByIdWithThrow(eq(TEST_CATE_ID))).willThrow(
                new NotFoundCategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY));

            //given(categoryRepository.findById(eq(TEST_CATE_ID))).willReturn(Optional.empty());
            //이거 오류뜸!!!
            // when
            GlobalException exception = assertThrows(GlobalException.class, () -> {
                adminCategoryService.updateCategory(1L, req);
            });

            // then
            assertEquals(CategoryErrorCode.NOT_FOUND_CATEGORY,
                exception.getErrorCode());
        }
    }

    @Nested
    class 삭제_테스트 {

        @DisplayName("카테고리 삭제")
        @Test
        void 카테고리_삭제() {
            // given
            ReflectionTestUtils.setField(TEST_CATE, "id", TEST_CATE_ID);
            given(categoryRepository.findCategoryByIdWithThrow(eq(TEST_CATE_ID))).willReturn(
                TEST_CATE);
            // when
            CategoryDeleteRes res = adminCategoryService.deleteCategory(TEST_CATE_ID);

            // then
            assertThat(res.message()).isEqualTo("카테고리가 삭제가 완료되었습니다.");
            Mockito.verify(categoryRepository, Mockito.times(1)).delete(any(Category.class));
        }

        @DisplayName("카테고리 삭제 사용중인 카테고리")
        @Test
        void 카테고리_삭제_사용중인카테고리() {
            // given
            ReflectionTestUtils.setField(TEST_CATE, "id", TEST_CATE_ID);
            given(categoryRepository.findCategoryByIdWithThrow(eq(TEST_CATE_ID))).willReturn(
                TEST_CATE);
            given(productCategoryRepository.existsByCategory_Id(TEST_CATE_ID)).willReturn(true);
            // when
            GlobalException exception = assertThrows(GlobalException.class, () -> {
                adminCategoryService.deleteCategory(1L);
            });

            // then
            assertEquals(CategoryErrorCode.FORBIDDEM_DELETE_CATEGORY,
                exception.getErrorCode());

        }
    }

    @Nested
    class 조회_테스트 {

        @DisplayName("카테고리 전체조회")
        @Test
        void 카테고리_전체조회() {

            // given

            given(categoryRepository.findAll()).willReturn(
                List.of(TEST_CATE, TEST_ANOTHER_CATE, TEST_ANOTHER_CATE2));

            // when
            List<CategoryListRes> res = categoryService.getCategoryList();

            // then
            assertThat(res.size()).isEqualTo(
                3);


        }
    }


}

