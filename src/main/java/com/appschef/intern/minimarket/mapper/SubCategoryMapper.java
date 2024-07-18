package com.appschef.intern.minimarket.mapper;

import com.appschef.intern.minimarket.dto.request.subCategories.AddSubCategoryRequest;
import com.appschef.intern.minimarket.dto.response.subCategory.SubCategoryDetailResponse;
import com.appschef.intern.minimarket.entity.SubCategories;

import java.util.Date;

public class SubCategoryMapper {
    public static SubCategories mapToSubCategories(AddSubCategoryRequest addSubCategoryRequest){
        return new SubCategories(
                null,
                addSubCategoryRequest.getSubCategoryCode(),
                addSubCategoryRequest.getSubCategoryName(),
                false,
                new Date(),
                null,
                null,
                null
        );
    }

    public static SubCategoryDetailResponse mapToSubCategoryDetailResponse(SubCategories subCategory){
        return new SubCategoryDetailResponse(
                subCategory.getCategories().getCategoryCode(),
                subCategory.getCategories().getCategoryName(),
                subCategory.getSubCategoryCode(),
                subCategory.getSubCategoryName()
        );
    }
}
