package com.example.booktalk.domain.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.exception.CategoryErrorCode;
import com.example.booktalk.domain.category.repository.CategoryRepository;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
import com.example.booktalk.domain.product.dto.request.ProductCreateReq;
import com.example.booktalk.domain.product.dto.request.ProductUpdateReq;
import com.example.booktalk.domain.product.dto.response.ProductCreateRes;
import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;
import com.example.booktalk.domain.product.dto.response.ProductGetRes;
import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.product.dto.response.ProductSerachListRes;
import com.example.booktalk.domain.product.dto.response.ProductTagListRes;
import com.example.booktalk.domain.product.dto.response.ProductUpdateRes;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.product.service.ProductService;
import com.example.booktalk.domain.productcategory.entity.ProductCategory;
import com.example.booktalk.domain.productcategory.repository.ProductCategoryRepository;
import com.example.booktalk.domain.test.ProductTest;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.exception.GlobalException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class) // junit5용

public class ProductServiceTest implements ProductTest {

    Product TEST_PRODCUT;
    Product TEST_ANOTHER_PRODCUT;
    Category TEST_CATE;
    Category TEST_ANOTHER_CATE;
    Category TEST_ANOTHER_CATE2;
    User TEST_USER = User.builder()
        .randomNickname(TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .role(UserRoleType.USER)
        .build();
    User TEST_ANOTHER_USER = User.builder()
        .randomNickname(ANOTHER_PREFIX + TEST_USER_NAME)
        .password(ANOTHER_PREFIX + TEST_USER_PASSWORD)
        .role(UserRoleType.USER)
        .build();
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductCategoryRepository productCategoryRepository;
    @Mock
    private ImageFileService imageFileService;
    @InjectMocks
    private ProductService productService;

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

        TEST_PRODCUT = Product.builder()
            .name(TEST_PRODUCT_NAME)
            .content(TEST_PRODUCT_CONTENT)
            .price(TEST_PRODUCT_PRICE)
            .quantity(TEST_PRODUCT_QUNANTITY)
            .region(TEST_PRODUCT_REGION)
            .user(TEST_USER)
            .build();

        TEST_ANOTHER_PRODCUT = Product.builder()
            .name(TEST_ANOTHER_PRODUCT_NAME)
            .content(TEST_ANOTHER_PRODUCT_CONTENT)
            .price(TEST_ANOTHER_PRODUCT_PRICE)
            .quantity(TEST_ANOTHER_PRODUCT_QUNANTITY)
            .region(TEST_ANOTHER_PRODUCT_REGION)
            .user(TEST_USER)
            .build();

    }


    @Nested
    class 생성_테스트 {

        @DisplayName("상품 생성")
        @Test
        void 상품생성() throws IOException {
            // given
            ProductCreateReq req = new ProductCreateReq(TEST_PRODUCT_NAME, TEST_PRODUCT_PRICE,
                TEST_PRODUCT_QUNANTITY
                , TEST_PRODUCT_REGION, TEST_PRODUCT_CONTENT, TEST_CATEGORY_LIST);

            ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);

            ReflectionTestUtils.setField(TEST_PRODCUT, "id", TEST_PRODUCT_ID);

            given(userRepository.findUserByIdWithThrow(eq(TEST_USER_ID))).willReturn(
                TEST_USER);

            given(categoryRepository.findByNameIn(eq(TEST_CATEGORY_LIST))).willReturn(
                List.of(TEST_CATE, TEST_ANOTHER_CATE));

            given(productRepository.save(any(Product.class))).willReturn(TEST_PRODCUT);

            // when
            ProductCreateRes res = productService.createProduct(TEST_USER_ID, req,
                TEST_MULTIPARTFILE_LIST);

            // then
            assertThat(res.id()).isEqualTo(TEST_PRODCUT.getId());
            assertThat(res.name()).isEqualTo(TEST_PRODCUT.getName());
            assertThat(res.user().name()).isEqualTo(TEST_PRODCUT.getUser().getNickname());
            Mockito.verify(productRepository, Mockito.times(1)).save(any(Product.class));
            Mockito.verify(productCategoryRepository, Mockito.times(2))
                .save(any(ProductCategory.class));
        }

        @DisplayName("상품 생성실패")
        @Test
        void 상품생성_실패_없는카테고리로_상품생성() {
            // given
            ProductCreateReq req = new ProductCreateReq(TEST_PRODUCT_NAME, TEST_PRODUCT_PRICE,
                TEST_PRODUCT_QUNANTITY
                , TEST_PRODUCT_REGION, TEST_PRODUCT_CONTENT, TEST_CATEGORY_LIST);

            ReflectionTestUtils.setField(TEST_PRODCUT, "id", TEST_PRODUCT_ID);

            given(categoryRepository.findByNameIn(eq(TEST_CATEGORY_LIST))).willReturn(
                Collections.emptyList());

            // when
            GlobalException exception = assertThrows(GlobalException.class, () -> {
                productService.createProduct(TEST_USER_ID, req, TEST_MULTIPARTFILE_LIST);
            });

            // then
            assertEquals(CategoryErrorCode.NOT_FOUND_CATEGORY,
                exception.getErrorCode());

        }

        @Nested
        class 수정_테스트 {

            @DisplayName("상품 수정 가격변경")
            @Test
            void 상품수정() throws IOException {
                // given
                ProductUpdateReq req = new ProductUpdateReq(TEST_PRODUCT_NAME,
                    TEST_ANOTHER_PRODUCT_PRICE,
                    TEST_PRODUCT_QUNANTITY
                    , TEST_PRODUCT_REGION, TEST_PRODUCT_CONTENT, false, TEST_CATEGORY_LIST);

                ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);
                ReflectionTestUtils.setField(TEST_PRODCUT, "id", TEST_PRODUCT_ID);

                given(productRepository.findProductByIdWithThrow(eq(TEST_PRODUCT_ID))).willReturn(
                    TEST_PRODCUT);

                given(userRepository.findUserByIdWithThrow(eq(TEST_USER_ID))).willReturn(TEST_USER);

                given(categoryRepository.findByNameIn(eq(TEST_CATEGORY_LIST)))
                    .willReturn(List.of(TEST_CATE, TEST_ANOTHER_CATE));

                //eq(TEST_CATEGORY_LIST)사용시오류
                ProductUpdateRes res = productService.updateProduct(TEST_USER_ID, TEST_PRODUCT_ID,
                    req, TEST_MULTIPARTFILE_LIST);

                // then
                assertThat(res.id()).isEqualTo(TEST_PRODCUT.getId());
                assertThat(res.price()).isEqualTo(TEST_PRODCUT.getPrice());
            }


            @DisplayName("상품 수정성공 카테고리변경")
            @Test
            void 상품수정성공_카테고리변경() throws IOException {
                // given
                ProductUpdateReq req = new ProductUpdateReq(TEST_PRODUCT_NAME,
                    TEST_ANOTHER_PRODUCT_PRICE,
                    TEST_ANOTHER_PRODUCT_QUNANTITY
                    , TEST_PRODUCT_REGION, TEST_PRODUCT_CONTENT, false, TEST_ANOTHER_CATEGORY_LIST);

                ReflectionTestUtils.setField(TEST_PRODCUT, "id", TEST_PRODUCT_ID);
                ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);
                given(productRepository.findProductByIdWithThrow(eq(TEST_PRODUCT_ID))).willReturn(
                    TEST_PRODCUT);

                given(userRepository.findUserByIdWithThrow(eq(TEST_USER_ID))).willReturn(
                    TEST_USER);

                given(categoryRepository.findByNameIn(eq(TEST_ANOTHER_CATEGORY_LIST))).willReturn(
                    List.of(TEST_CATE, TEST_ANOTHER_CATE2));

                // when
                ProductUpdateRes res = productService.updateProduct(TEST_USER_ID, TEST_PRODUCT_ID,
                    req, TEST_MULTIPARTFILE_LIST);

                // then
                assertThat(res.id()).isEqualTo(TEST_PRODCUT.getId());
                assertThat(res.price()).isEqualTo(TEST_PRODCUT.getPrice());

                // then

            }

            @DisplayName("상품 수정실패 작성자아님")
            @Test
            void 상품수정실패_작성자아님() throws IOException {
                // given
                ProductUpdateReq req = new ProductUpdateReq(TEST_PRODUCT_NAME,
                    TEST_ANOTHER_PRODUCT_PRICE,
                    TEST_ANOTHER_PRODUCT_QUNANTITY
                    , TEST_PRODUCT_REGION, TEST_PRODUCT_CONTENT, false, TEST_ANOTHER_CATEGORY_LIST);

                ReflectionTestUtils.setField(TEST_PRODCUT, "id", TEST_PRODUCT_ID);
                ReflectionTestUtils.setField(TEST_ANOTHER_USER, "id", TEST_ANOTHER_USER_ID);
                given(productRepository.findProductByIdWithThrow(eq(TEST_PRODUCT_ID))).willReturn(
                    TEST_PRODCUT);

                given(userRepository.findUserByIdWithThrow(eq(TEST_ANOTHER_USER_ID))).willReturn(
                    TEST_ANOTHER_USER);

                // when
                GlobalException exception = assertThrows(GlobalException.class, () -> {
                    ProductUpdateRes res = productService.updateProduct(TEST_ANOTHER_USER_ID,
                        TEST_PRODUCT_ID,
                        req, TEST_MULTIPARTFILE_LIST);
                });

                // then
                assertEquals(ProductErrorCode.NOT_PERMISSION_AUTHORITHY,
                    exception.getErrorCode());
            }


        }

        @Nested
        class 삭제_테스트 {

            @DisplayName("상품 삭제")
            @Test
            void 상품삭제() {

                // given

                ReflectionTestUtils.setField(TEST_USER, "id", TEST_USER_ID);
                ReflectionTestUtils.setField(TEST_PRODCUT, "id", TEST_PRODUCT_ID);

                given(productRepository.findProductByIdWithThrow(eq(TEST_PRODUCT_ID))).willReturn(
                    TEST_PRODCUT);

                given(userRepository.findUserByIdWithThrow(eq(TEST_USER_ID))).willReturn(TEST_USER);

                ProductDeleteRes res = productService.deleteProduct(TEST_USER_ID, TEST_PRODUCT_ID);

                // then
                assertThat(res.message()).isEqualTo("삭제가 완료되었습니다.");
                assertThat(TEST_PRODCUT.getDeleted()).isEqualTo(true);


            }

        }


        @Nested
        class 조회_테스트 {

            @DisplayName("상품 단일조회")
            @Test
            void 상품단일조회() {

                // given
                ReflectionTestUtils.setField(TEST_PRODCUT, "id", TEST_PRODUCT_ID);

                given(productRepository.findProductByIdWithThrow(eq(TEST_PRODUCT_ID))).willReturn(
                    TEST_PRODCUT);

                //when

                ProductGetRes res = productService.getProduct(TEST_PRODUCT_ID);

                // then
                assertThat(res.name()).isEqualTo(TEST_PRODCUT.getName());
                assertThat(res.user().name()).isEqualTo(TEST_PRODCUT.getUser().getNickname());


            }

            @DisplayName("상품 리스트조회_가격순으로내림차순정렬")
            @Test
            void 상품_리스트조회() {

                // given
                List<Product> mockProductList = new ArrayList<>(
                    List.of(TEST_PRODCUT, TEST_ANOTHER_PRODCUT));

                //when
                mockProductList.sort(Comparator.comparing(Product::getPrice).reversed());
                when(productRepository.findAllByDeletedFalse(any(Pageable.class))).thenReturn(
                    new PageImpl<>(mockProductList));
                Page<ProductListRes> res = productService.getProductList(0, 10, "price", false);

                // then
                assertThat(res.getContent()).hasSize(2);
                ProductListRes firstProduct = res.getContent().get(0);
                assertThat(firstProduct.name()).isEqualTo(TEST_ANOTHER_PRODCUT.getName());

                ProductListRes secondProduct = res.getContent().get(1);
                assertThat(secondProduct.name()).isEqualTo(TEST_PRODCUT.getName());


            }

            @DisplayName("상품 리스트조회_제목검색조회")
            @Test
            void 상품_리스트조회_제목검색() {

                // given
                List<Product> mockProductList = new ArrayList<>(
                    List.of(TEST_PRODCUT, TEST_ANOTHER_PRODCUT));

                //when
                when(productRepository.getPostListByName(any(Pageable.class), anyString()))
                    .thenReturn(new PageImpl<>(mockProductList));
                Page<ProductSerachListRes> res = productService.getProductSearchList(0, 10, "price",
                    false, TEST_PRODUCT_NAME);

                // then
                ProductSerachListRes firstProduct = res.getContent().get(0);
                assertThat(firstProduct.name()).isEqualTo(TEST_PRODCUT.getName());

            }


            @DisplayName("상품 리스트조회_태그검색조회")
            @Test
            void 상품_리스트조회_태그검색() {

                // given
                List<Product> mockProductList = new ArrayList<>(
                    List.of(TEST_PRODCUT, TEST_ANOTHER_PRODCUT));

                //when
                when(productRepository.getProductListByTag(any(Pageable.class), anyString()))
                    .thenReturn(new PageImpl<>(mockProductList));
                Page<ProductTagListRes> res = productService.getProductSearchTagList(0, 10, "price",
                    false, TEST_CATE.getName());

                // then
                ProductTagListRes firstProduct = res.getContent().get(0);
                assertThat(firstProduct.name()).isEqualTo(TEST_PRODCUT.getName());

            }
        }
    }
}
