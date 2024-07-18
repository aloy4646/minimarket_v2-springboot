package com.appschef.intern.minimarket.dto.request.categories;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryRequest {
    @NotBlank(message = "category_code must be provided")
    @Size(max = 7, message = "category_code cannot exceed 7 characters")
    @JsonProperty("category_code")
    private String categoryCode;

    @NotBlank(message = "category_name must be provided")
    @Size(max = 100, message = "category_name cannot exceed 100 characters")
    @JsonProperty("category_name")
    private String categoryName;
}
