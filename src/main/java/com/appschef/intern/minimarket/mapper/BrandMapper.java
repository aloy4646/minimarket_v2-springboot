package com.appschef.intern.minimarket.mapper;

import com.appschef.intern.minimarket.dto.request.brand.AddBrandRequest;
import com.appschef.intern.minimarket.dto.response.brand.BrandDetailResponse;
import com.appschef.intern.minimarket.entity.Brand;

import java.util.Date;

public class BrandMapper {
    public static Brand mapToBrand(AddBrandRequest addBrandRequest){
        return new Brand(
                null,
                addBrandRequest.getBrandCode(),
                addBrandRequest.getBrandName(),
                false,
                new Date(),
                null,
                null
        );
    }

    public static BrandDetailResponse mapToBrandDetailResponse(Brand brand){
        return new BrandDetailResponse(
                brand.getBrandCode(),
                brand.getBrandName()
        );
    }
}
