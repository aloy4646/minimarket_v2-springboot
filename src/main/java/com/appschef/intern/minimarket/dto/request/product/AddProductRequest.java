package com.appschef.intern.minimarket.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    @NotBlank(message = "full_name must be provided")
    @Size(max = 100, message = "full_name cannot exceed 100 characters")


    @NotBlank(message = "product_code must be provided")
    @Size(max = 13, message = "product_code cannot exceed 13 characters")
    @JsonProperty("product_code")
    private String productCode;

    @NotBlank(message = "product_name must be provided")
    @Size(max = 100, message = "product_name cannot exceed 100 characters")
    @JsonProperty("product_name")
    private String productName;

    @NotNull(message = "price must be provided")
    @PositiveOrZero(message = "price must be greater than or equal to 0")
    @JsonProperty("price")
    private BigDecimal price;

    @NotBlank(message = "brand_code must be provided")
    @Size(max = 7, message = "brand_code cannot exceed 7 characters")
    @JsonProperty("brand_code")
    private String brandCode;

    @NotBlank(message = "subcategory_code must be provided")
    @Size(max = 7, message = "subcategory_code cannot exceed 7 characters")
    @JsonProperty("subcategory_code")
    private String subCategoryCode;
}
