package com.appschef.intern.minimarket.dto.request.brand;

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
public class AddBrandRequest {
    @NotBlank(message = "brand_code must be provided")
    @Size(max = 7, message = "brand_code cannot exceed 7 characters")
    @JsonProperty("brand_code")
    private String brandCode;

    @NotBlank(message = "brand_name must be provided")
    @Size(max = 100, message = "brand_name cannot exceed 100 characters")
    @JsonProperty("brand_name")
    private String brandName;
}
