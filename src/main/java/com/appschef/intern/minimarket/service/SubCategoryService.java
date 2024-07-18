package com.appschef.intern.minimarket.service;

import com.appschef.intern.minimarket.dto.request.subCategories.AddSubCategoryRequest;
import com.appschef.intern.minimarket.dto.request.subCategories.UpdateSubCategoryRequest;
import com.appschef.intern.minimarket.dto.response.subCategory.SubCategoryDetailResponse;
import com.appschef.intern.minimarket.entity.Categories;
import com.appschef.intern.minimarket.entity.SubCategories;
import com.appschef.intern.minimarket.enumMessage.CategoriesErrorMessage;
import com.appschef.intern.minimarket.enumMessage.SubCategoryErrorMessage;
import com.appschef.intern.minimarket.mapper.SubCategoryMapper;
import com.appschef.intern.minimarket.repository.CategoriesRepository;
import com.appschef.intern.minimarket.repository.SubCategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Transactional
    public SubCategoryDetailResponse addSubCategory(AddSubCategoryRequest addSubCategoryRequest) {
        SubCategories subCategories = SubCategoryMapper.mapToSubCategories(addSubCategoryRequest);

        Categories categories = categoriesRepository.findByCategoryCodeAndIsDeletedFalse(addSubCategoryRequest.getCategoryCode())
                .orElseThrow(() -> new ValidationException(CategoriesErrorMessage.NOT_FOUND.getMessage()));

        //subCategoryCode duplication check
        if(subCategoryRepository.existsBySubCategoryCode(subCategories.getSubCategoryCode())){
            throw new ValidationException(SubCategoryErrorMessage.EXIST.getMessage());
        }

        subCategories.setCategories(categories);

        subCategoryRepository.saveAndFlush(subCategories);
        return SubCategoryMapper.mapToSubCategoryDetailResponse(subCategories);
    }

    public SubCategoryDetailResponse getSubCategoryBySubCategoryCode(String subCategoryCode) {
        SubCategories subCategories = subCategoryRepository.findBySubCategoryCodeAndIsDeletedFalse(subCategoryCode)
                .orElseThrow(() -> new ValidationException(SubCategoryErrorMessage.NOT_FOUND.getMessage()));

        return SubCategoryMapper.mapToSubCategoryDetailResponse(subCategories);
    }

    public Page<SubCategoryDetailResponse> getAllSubCategory(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "subCategoryName"));
        Page<SubCategories> listSubCategory = subCategoryRepository.findByIsDeletedFalse(pageable);
        return listSubCategory.map(SubCategoryMapper::mapToSubCategoryDetailResponse);
    }

    @Transactional
    public SubCategoryDetailResponse updateSubCategory(String subCategoryCode, UpdateSubCategoryRequest newSubCategory) {
        SubCategories oldSubCategories = subCategoryRepository.findBySubCategoryCodeAndIsDeletedFalse(subCategoryCode)
                .orElseThrow(() -> new ValidationException(SubCategoryErrorMessage.NOT_FOUND.getMessage()));

        oldSubCategories.setSubCategoryName(newSubCategory.getSubCategoryName());
        oldSubCategories.setUpdatedAt(new Date());

        subCategoryRepository.saveAndFlush(oldSubCategories);

        return SubCategoryMapper.mapToSubCategoryDetailResponse(oldSubCategories);
    }

    @Transactional
    public void deleteSubCategory(String subCategoryCode) {
        Long idSubCategory = subCategoryRepository.findIdBySubCategoryCodeAndIsDeletedFalse(subCategoryCode)
                .orElseThrow(() -> new ValidationException(SubCategoryErrorMessage.NOT_FOUND.getMessage()));

        int affectedRows = subCategoryRepository.softDeleteById(idSubCategory);
        if (affectedRows == 0){
            throw new ValidationException(SubCategoryErrorMessage.FAILED_DELETE.getMessage());
        }
    }
}