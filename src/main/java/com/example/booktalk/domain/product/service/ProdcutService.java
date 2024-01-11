package com.example.booktalk.domain.product.service;

import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.exception.CategoryErrorCode;
import com.example.booktalk.domain.category.exception.NotFoundCategoryException;
import com.example.booktalk.domain.category.repository.CategoryRepository;
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
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.NotPermissionAuthority;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.productcategory.entity.ProductCategory;
import com.example.booktalk.domain.productcategory.repository.ProductCategoryRepository;
import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProdcutService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCreateRes createProduct(Long userId, ProductCreateReq req) {

        User user = findUser(userId);

        Product product = Product.builder()
            .name(req.name())
            .price(req.price())
            .quantity(req.quantity())
            .region(req.region())
            .user(user)
            .build();
        productRepository.save(product);
        addCategory(req.categoryList(), product);

        UserRes userRes = new UserRes(user.getId(), user.getNickname());

        return new ProductCreateRes(product.getId(), product.getName(), product.getQuantity(),
            product.getPrice()
            , product.getRegion(), product.getFinished(), userRes,
            req.categoryList());
        //TODO 생성자로 한줄정리

    }

    public ProductUpdateRes updateProduct(Long userId, Long productId, ProductUpdateReq req) {

        User user = findUser(userId);
        Product product = findProduct(productId);
        validateProductUser(user, product);

        product.update(req);
        updateCategory(req.categoryList(), product);
        UserRes userRes = new UserRes(user.getId(), user.getNickname());
        return new ProductUpdateRes(product.getId(), product.getName(),
            product.getQuantity(), product.getPrice(), product.getRegion(),
            product.getFinished(), userRes, product.getProductLikeCnt(), req.categoryList());

    }

    @Transactional(readOnly = true)
    public ProductGetRes getProduct(Long productId) {

        Product product = findProduct(productId);
        User user = product.getUser();
        UserRes userRes = new UserRes(user.getId(), user.getNickname());

        List<String> categories = product.getProductCategoryList().stream()
            .map(productCategory -> {
                return productCategory.getCategory().getName();
            })
            .toList();

        return new ProductGetRes(product.getId(), product.getName(), product.getPrice()
            , product.getQuantity(), userRes, product.getRegion(), categories,
            product.getProductLikeCnt(),
            product.getFinished());

    }

    @Transactional(readOnly = true)
    public List<ProductListRes> getProductList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.findAllByDeletedFalse(sort);

        return productList.stream()
            .map(ProductListRes::new)
            .toList();

    }

    public List<ProductSerachListRes> getProductSearchList(String sortBy, Boolean isAsc,
        String search) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.getPostListByName(sort, search);
        return productList.stream()
            .map(ProductSerachListRes::new)
            .toList();
    }


    public List<ProductTagListRes> getProductSearchTagList(String sortBy, Boolean isAsc,
        String tag) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.getProductListByTag(sort, tag);
        return productList.stream()
            .map(ProductTagListRes::new)
            .toList();
    }


    public ProductDeleteRes deleteProduct(Long userId, Long productId) {
        User user = findUser(userId);
        Product product = findProduct(productId);
        validateProductUser(user, product);

        product.deleted();

        return new ProductDeleteRes("삭제가 완료되었습니다.");

    }


    private User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당하는유저가 없습니다."));
    }

    private Product findProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));

        if (product.getDeleted()) {
            throw new GlobalException(ProductErrorCode.DELETED_PRODUCT);
        }
        return product;
    }

    private void validateProductUser(User user, Product product) {

        if (!user.getId().equals(product.getUser().getId())) {
            throw new NotPermissionAuthority(ProductErrorCode.NOT_PERMISSION_AUTHORITHY);
        }
    }

    private List<Category> findCategoryList(List<String> categoryList) {
        List<Category> categories = categoryRepository.findByNameIn(categoryList);

        if (categoryList.size() != categories.size()) {
            throw new NotFoundCategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY);
        }

        return categories;
    }

    private void addCategory(List<String> categoryList, Product product) {
        List<Category> categories = findCategoryList(categoryList);

        categories.forEach(category -> {
            ProductCategory productCategory = ProductCategory.builder()
                .category(category)
                .product(product)
                .build();

            productCategoryRepository.save(productCategory);
            product.addProductCategory(productCategory);
        });
    }


    private void removeCategory(List<String> categoryList, Product product) {

        List<ProductCategory> productCategoryList = productCategoryRepository.findAllByProductAndCategory_NameIn(
            product, categoryList);

        productCategoryList.forEach(productCategory ->
        {
            productCategoryRepository.delete(productCategory);
            //product.removeProductCategory(productCategory); remove도 따로 만들어줘야할까..?
        });


    }


    private void updateCategory(List<String> reqCategoryList, Product product) {

        List<String> currentCategories = product.getProductCategoryList()
            .stream() //성능이슈 확인 N + 1
            .map(ProductCategory::getCategory)
            .map(Category::getName).toList();

        List<String> removeableCategoryList = currentCategories.stream()
            .filter(category -> !reqCategoryList.contains(category))
            .toList();

        removeCategory(removeableCategoryList, product);

        List<String> addableCategoryList = reqCategoryList.stream()
            .filter(category -> !currentCategories.contains(category))
            .toList();

        addCategory(addableCategoryList, product);

    }

}
