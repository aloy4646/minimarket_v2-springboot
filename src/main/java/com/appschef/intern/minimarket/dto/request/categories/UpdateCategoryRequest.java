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
public class UpdateCategoryRequest {
    @NotBlank(message = "category_name must be provided")
    @Size(max = 100, message = "category_name cannot exceed 100 characters")
    @JsonProperty("category_name")
    private String categoryName;
}
