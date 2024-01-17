package com.example.booktalk.domain.product.service;

import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.exception.CategoryErrorCode;
import com.example.booktalk.domain.category.exception.NotFoundCategoryException;
import com.example.booktalk.domain.category.repository.CategoryRepository;
import com.example.booktalk.domain.imageFile.dto.response.ImageCreateRes;
import com.example.booktalk.domain.imageFile.dto.response.ImageListRes;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
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
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotPermissionAuthority;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.productcategory.entity.ProductCategory;
import com.example.booktalk.domain.productcategory.repository.ProductCategoryRepository;
import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ImageFileService imageFileService;

    public ProductCreateRes createProduct(Long userId, ProductCreateReq req,
        List<MultipartFile> files) throws IOException {

        User user = userRepository.findUserByIdWithThrow(userId);

        Product product = Product.builder()
            .name(req.name())
            .price(req.price())
            .quantity(req.quantity())
            .region(req.region())
            .content(req.content())
            .user(user)
            .build();
        product = productRepository.save(product);

        addCategory(req.categoryList(), product);
        UserRes userRes = new UserRes(user.getId(), user.getNickname());

        List<ImageCreateRes> imageCreateResList = imageFileService.createImage(userId,
            product.getId(), files);

        return new ProductCreateRes(product.getId(), product.getName(), product.getQuantity(),
            product.getPrice()
            , product.getRegion(), product.getFinished(), userRes, product.getContent(),
            req.categoryList(), imageCreateResList);
        //TODO 생성자로 한줄정리

    }

    public ProductUpdateRes updateProduct(Long userId, Long productId, ProductUpdateReq req,
        List<MultipartFile> files) throws IOException {

        User user = userRepository.findUserByIdWithThrow(userId);
        Product product = productRepository.findProductByIdWithThrow(productId);
        validateProductUser(user, product);

        List<ImageCreateRes> imageCreateResList = imageFileService.updateImage(userId, productId,
            files);

        product.update(req);
        updateCategory(req.categoryList(), product);
        UserRes userRes = new UserRes(user.getId(), user.getNickname());
        return new ProductUpdateRes(product.getId(), product.getName(),
            product.getQuantity(), product.getPrice(), product.getRegion(),
            product.getFinished(), userRes, product.getProductLikeCnt(), product.getContent(),
            req.categoryList(), imageCreateResList);

    }

    @Transactional(readOnly = true)
    public ProductGetRes getProduct(Long productId) {

        Product product = productRepository.findProductByIdWithThrow(productId);
        User user = product.getUser();
        UserRes userRes = new UserRes(user.getId(), user.getNickname());

        List<String> categories = product.getProductCategoryList().stream()
            .map(productCategory -> {
                return productCategory.getCategory().getName();
            })
            .toList();
        List<ImageListRes> imageListRes = imageFileService.getImages(productId);

        return new ProductGetRes(product.getId(), product.getName(), product.getPrice()
            , product.getQuantity(), userRes, product.getRegion(), categories,
            product.getProductLikeCnt(), product.getContent(),
            product.getFinished(), imageListRes);

    }

    @Transactional(readOnly = true)
    public List<ProductListRes> getProductList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.findAllByDeletedFalse(sort);

        return productList.stream()
            .map(product -> {
                List<ImageListRes> imageListRes = imageFileService.getImages(product.getId());

                List<String> categories = product.getProductCategoryList().stream()
                    .map(productCategory -> {
                        return productCategory.getCategory().getName();
                    })
                    .toList();
                ImageListRes imageGetRes = imageListRes.isEmpty() ? null : imageListRes.get(0);

                return new ProductListRes(product.getId(), product.getName(), product.getPrice(),
                    product.getQuantity(), product.getProductLikeCnt(), categories,
                    product.getRegion(), imageGetRes);
            })
            .toList();

    }

    @Transactional(readOnly = true)
    public List<ProductTopLikesListRes> getProductListByLikesTopThree() {

        List<Product> productList = productRepository.findTop3ByDeletedFalseOrderByProductLikeCntDesc();

        return productList.stream()
            .map(product -> {

                List<ImageListRes> imageListRes = imageFileService.getImages(product.getId());

                List<String> categories = product.getProductCategoryList().stream()
                    .map(productCategory -> {
                        return productCategory.getCategory().getName();
                    })
                    .toList();
                ImageListRes imageGetRes =
                    imageListRes.isEmpty() ? new ImageListRes(null) : imageListRes.get(0);

                return new ProductTopLikesListRes(product.getId(), product.getName(),
                    product.getPrice(),
                    product.getQuantity(), product.getProductLikeCnt(), categories,
                    product.getRegion(), imageGetRes);

            })
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductSerachListRes> getProductSearchList(String sortBy, Boolean isAsc,
        String search) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.getPostListByName(sort, search);
        return productList.stream()
            .map(product -> {

                List<String> categories = product.getProductCategoryList().stream()
                    .map(productCategory -> {
                        return productCategory.getCategory().getName();
                    })
                    .toList();

                return new ProductSerachListRes(product.getId(), product.getName(),
                    product.getPrice(),
                    product.getQuantity(), product.getProductLikeCnt(), categories,
                    product.getRegion());
            })
            .toList();

    }

    @Transactional(readOnly = true)
    public List<ProductTagListRes> getProductSearchTagList(String sortBy, Boolean isAsc,
        String tag) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.getProductListByTag(sort, tag);
        return productList.stream()
            .map(product -> {

                List<String> categories = product.getProductCategoryList().stream()
                    .map(productCategory -> {
                        return productCategory.getCategory().getName();
                    })
                    .toList();

                return new ProductTagListRes(product.getId(), product.getName(), product.getPrice(),
                    product.getQuantity(), product.getProductLikeCnt(), categories,
                    product.getRegion());
            })
            .toList();

    }


    public ProductDeleteRes deleteProduct(Long userId, Long productId) {
        User user = userRepository.findUserByIdWithThrow(userId);
        Product product = productRepository.findProductByIdWithThrow(productId);
        validateProductUser(user, product);
        imageFileService.deleteImage(userId, productId);

        product.deleted();

        return new ProductDeleteRes("삭제가 완료되었습니다.");

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
