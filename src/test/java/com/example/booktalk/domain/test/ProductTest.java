package com.example.booktalk.domain.test;

import com.example.booktalk.domain.product.entity.Region;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductTest extends CommonTest, CategoryTest, ImageTest {

    Long TEST_PRODUCT_ID = 1L;
    String TEST_PRODUCT_NAME = "name";
    Long TEST_PRODUCT_PRICE = 5000L;
    Long TEST_PRODUCT_QUNANTITY = 3L;
    String TEST_PRODUCT_CONTENT = "content";
    Region TEST_PRODUCT_REGION = Region.SEOUL;
    List<String> TEST_CATEGORY_LIST = List.of(TEST_CATE_NAME, TEST_CATE_NAME2);
    List<MultipartFile>TEST_MULTIPARTFILE_LIST = List.of(FILE);


    Long TEST_ANOTHER_PRODUCT_ID = 2L;
    String TEST_ANOTHER_PRODUCT_NAME = "name2";
    String TEST_ANOTHER_PRODUCT_CONTENT = "content2";
    Long TEST_ANOTHER_PRODUCT_PRICE = 6000L;
    Long TEST_ANOTHER_PRODUCT_QUNANTITY = 5L;
    Region TEST_ANOTHER_PRODUCT_REGION = Region.BUSAN;
    List<String> TEST_ANOTHER_CATEGORY_LIST = List.of(TEST_CATE_NAME,
        TEST_CATE_NAME3);


}
