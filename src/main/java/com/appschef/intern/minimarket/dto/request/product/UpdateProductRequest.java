package com.appschef.intern.minimarket.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    @NotBlank(message = "product_name must be provided")
    @Size(max = 100, message = "product_name cannot exceed 100 characters")
    @JsonProperty("product_name")
    private String productName;

    @NotNull(message = "price must be provided")
    @PositiveOrZero(message = "price must be greater than or equal to 0")
    @JsonProperty("price")
    private BigDecimal price;
}
