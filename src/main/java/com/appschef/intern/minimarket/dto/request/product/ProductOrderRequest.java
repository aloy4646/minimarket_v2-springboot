package com.appschef.intern.minimarket.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderRequest {
    @NotBlank(message = "product_code must be provided")
    @Size(max = 13, message = "product_code cannot exceed 13 characters")
    @JsonProperty("product_code")
    private String productCode;

    @NotNull(message = "quantity must be provided")
    @Positive(message = "quantity must be greater than 0")
    @JsonProperty("quantity")
    private Integer quantity;
}
