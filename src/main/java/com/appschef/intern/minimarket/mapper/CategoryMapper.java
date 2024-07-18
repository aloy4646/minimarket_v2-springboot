package com.appschef.intern.minimarket.mapper;

import com.appschef.intern.minimarket.dto.request.categories.AddCategoryRequest;
import com.appschef.intern.minimarket.dto.response.categories.CategoryDetailResponse;
import com.appschef.intern.minimarket.entity.Categories;

import java.util.Date;

public class CategoryMapper {
    public static Categories mapToCategory(AddCategoryRequest addCategoryRequest){
        return new Categories(
                null,
                addCategoryRequest.getCategoryCode(),
                addCategoryRequest.getCategoryName(),
                false,
                new Date(),
                null,
                null
        );
    }

    public static CategoryDetailResponse mapToCategoryDetailResponse(Categories categories){
        return new CategoryDetailResponse(
                categories.getCategoryCode(),
                categories.getCategoryName()
        );
    }
}
