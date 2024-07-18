package com.appschef.intern.minimarket.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("current_stock")
    private Integer currentStock;

    @JsonProperty("brand_code")
    private String brandCode;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("category_code")
    private String categoryCode;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("subcategory_code")
    private String subCategoryCode;

    @JsonProperty("subcategory_name")
    private String subCategoryName;

}
