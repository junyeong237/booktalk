package com.example.booktalk.domain.product.service;

import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.exception.CategoryErrorCode;
import com.example.booktalk.domain.category.exception.NotFoundCategoryException;
import com.example.booktalk.domain.category.repository.CategoryRepository;
import com.example.booktalk.domain.product.dto.request.ProductRegisterReq;
import com.example.booktalk.domain.product.dto.request.ProductUpdateReq;
import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;
import com.example.booktalk.domain.product.dto.response.ProductGetRes;
import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.product.dto.response.ProductRegisterRes;
import com.example.booktalk.domain.product.dto.response.ProductUpdateRes;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.NotPermissionAuthority;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.productcategory.entity.ProductCategory;
import com.example.booktalk.domain.productcategory.repository.ProductCategoryRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Stream;
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

    public ProductRegisterRes registerProduct(Long userId, ProductRegisterReq req) {

        User user = findUser(userId);

        Product product = Product.builder()
            .name(req.name())
            .price(req.price())
            .quantity(req.quantity())
            .regions(req.regions())
            .user(user)
            .build();

        addCategory(req.categoryList(), product);

        productRepository.save(product);

        return new ProductRegisterRes(product.getId(), product.getName(), product.getQuantity()
            , product.getRegions(), product.getFinished(), user,
            req.categoryList());
        // prodcut.getUser()와 user의 성능차이
        // req.categoryList() 와 product.getProdcutCategoryList() 성능차이

    }

    public ProductUpdateRes updateRegister(Long userId, Long productId, ProductUpdateReq req) {

        User user = findUser(userId);
        Product product = findProduct(productId);
        validateProductUser(user, product);

        product.update(req);
        updateCategory(req.categoryList(), product);

        return new ProductUpdateRes(product.getId(), product.getName(),
            product.getQuantity(), product.getRegions(),
            product.getFinished(), user, req.categoryList());

    }

    @Transactional(readOnly = true)
    public ProductGetRes getProduct(Long productId) {

        Product product = findProduct(productId);

        return new ProductGetRes(product.getId(), product.getName(), product.getPrice()
            , product.getQuantity(), product.getRegions(), product.getFinished());

    }

    @Transactional(readOnly = true)
    public List<ProductListRes> getProductList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.findAll(sort);

        return productList.stream()
            .map(
                product -> new ProductListRes(product.getId(), product.getName()
                    , product.getPrice(), product.getQuantity(), product.getRegions())
            )
            .toList();

    }

//    public List<ProductSerachListRes> getProductSearchList(String sortBy, Boolean isAsc,
//        String search) {
//
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);
//
//        List<Product> productList = productRepository.findAllBySearch(search, sort);
//
//    }


    public ProductDeleteRes deleteProduct(Long userId, Long productId) {
        User user = findUser(userId);
        Product product = findProduct(productId);
        validateProductUser(user, product);

        productRepository.delete(product);

        return new ProductDeleteRes("삭제가 완료되었습니다.");

    }


    private User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당하는유저가 없습니다."));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
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


    private void updateCategory(List<String> categoryList, Product product) {

        Stream<String> currentCategories = product.getProductCategoryList()
            .stream() //성능이슈 확인 N + 1
            .map(ProductCategory::getCategory)
            .map(Category::getName);

        List<String> removeableCategoryList = currentCategories
            .filter(category -> !categoryList.contains(category))
            .toList();

        removeCategory(removeableCategoryList, product);

        List<String> addableCategoryList = currentCategories
            .filter(category -> categoryList.contains(category))
            .toList();

        addCategory(addableCategoryList, product);

    }

}
