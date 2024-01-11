package com.example.booktalk.domain.category.repository;

import com.example.booktalk.domain.category.entity.Category;
import com.example.booktalk.domain.category.exception.CategoryErrorCode;
import com.example.booktalk.domain.category.exception.NotFoundCategoryException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String name);

    // 해당하는 name 값들을 가진 Category 엔터티 리스트를 조회하는 메서드
    List<Category> findByNameIn(List<String> names);

    default Category findCategoryByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new NotFoundCategoryException(CategoryErrorCode.NOT_FOUND_CATEGORY));
    }


}
