package com.appschef.intern.minimarket.service;

import com.appschef.intern.minimarket.dto.request.categories.AddCategoryRequest;
import com.appschef.intern.minimarket.dto.request.categories.UpdateCategoryRequest;
import com.appschef.intern.minimarket.dto.response.categories.CategoryDetailResponse;
import com.appschef.intern.minimarket.entity.Categories;
import com.appschef.intern.minimarket.enumMessage.CategoriesErrorMessage;
import com.appschef.intern.minimarket.mapper.CategoryMapper;
import com.appschef.intern.minimarket.repository.CategoriesRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Transactional
    public CategoryDetailResponse addCategory(AddCategoryRequest addCategoryRequest) {
        Categories categories = CategoryMapper.mapToCategory(addCategoryRequest);

        //pengecekan categoryCode duplikat
        if(categoriesRepository.existsByCategoryCode(categories.getCategoryCode())){
            throw new ValidationException(CategoriesErrorMessage.EXIST.toString());
        }

        categoriesRepository.saveAndFlush(categories);
        return CategoryMapper.mapToCategoryDetailResponse(categories);
    }

    public CategoryDetailResponse getCategoryByCategoryCode(String categoryCode) {
        Categories categories = categoriesRepository.findByCategoryCodeAndIsDeletedFalse(categoryCode)
                .orElseThrow(() -> new ValidationException(CategoriesErrorMessage.NOT_FOUND.getMessage()));

        return CategoryMapper.mapToCategoryDetailResponse(categories);
    }

    public Page<CategoryDetailResponse> getAllCategory(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "categoryName"));
        Page<Categories> listCategory = categoriesRepository.findByIsDeletedFalse(pageable);
        return listCategory.map(CategoryMapper::mapToCategoryDetailResponse);
    }

    @Transactional
    public CategoryDetailResponse updateCategory(String categoryCode, UpdateCategoryRequest newCategory) {
        Categories oldCategories = categoriesRepository.findByCategoryCodeAndIsDeletedFalse(categoryCode)
                .orElseThrow(() -> new ValidationException(CategoriesErrorMessage.NOT_FOUND.getMessage()));

        oldCategories.setCategoryName(newCategory.getCategoryName());
        oldCategories.setUpdatedAt(new Date());

        categoriesRepository.saveAndFlush(oldCategories);

        return CategoryMapper.mapToCategoryDetailResponse(oldCategories);
    }

    @Transactional
    public void deleteCategory(String categoryCode) {
        Long idCategory = categoriesRepository.findIdByCategoryCodeAndIsDeletedFalse(categoryCode)
                .orElseThrow(() -> new ValidationException(CategoriesErrorMessage.NOT_FOUND.getMessage()));

        int affectedRows = categoriesRepository.softDeleteById(idCategory);
        if (affectedRows == 0){
            throw new ValidationException(CategoriesErrorMessage.GAGAL_HAPUS.getMessage());
        }
    }
}
