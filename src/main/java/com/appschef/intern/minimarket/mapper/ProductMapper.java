package com.appschef.intern.minimarket.mapper;

import com.appschef.intern.minimarket.dto.request.product.AddProductRequest;
import com.appschef.intern.minimarket.dto.response.product.ProductDetailResponse;
import com.appschef.intern.minimarket.entity.Product;

import java.util.Date;

public class ProductMapper {
    public static Product mapToProduct(AddProductRequest addProdukRequest){

        return new Product(
                null,
                addProdukRequest.getProductCode(),
                addProdukRequest.getProductName(),
                addProdukRequest.getPrice(),
                0,
                false,
                new Date(),
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static ProductDetailResponse mapToProductDetailResponse(Product product){

        return new ProductDetailResponse(
                product.getProductCode(),
                product.getProductName(),
                product.getPrice(),
                product.getCurrentStock(),
                product.getBrand().getBrandCode(),
                product.getBrand().getBrandName(),
                product.getSubCategories().getCategories().getCategoryCode(),
                product.getSubCategories().getCategories().getCategoryName(),
                product.getSubCategories().getSubCategoryCode(),
                product.getSubCategories().getSubCategoryName()
        );
    }
}
