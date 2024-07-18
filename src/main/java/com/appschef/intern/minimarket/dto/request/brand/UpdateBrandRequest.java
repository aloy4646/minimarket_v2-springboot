package com.appschef.intern.minimarket.dto.request.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBrandRequest {
    @NotBlank(message = "brand_name must be provided")
    @Size(max = 100, message = "brand_name cannot exceed 100 characters")
    @JsonProperty("brand_name")
    private String brandName;
}
