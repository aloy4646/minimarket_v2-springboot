package com.appschef.intern.minimarket.dto.request.subCategories;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSubCategoryRequest {
    @NotBlank(message = "subcategory_code must be provided")
    @Size(max = 7, message = "subcategory_code cannot exceed 7 characters")
    @JsonProperty("subcategory_code")
    private String subCategoryCode;

    @NotBlank(message = "subcategory_name must be provided")
    @Size(max = 100, message = "subcategory_name cannot exceed 100 characters")
    @JsonProperty("subcategory_name")
    private String subCategoryName;

    @NotBlank(message = "category_code must be provided")
    @Size(max = 7, message = "category_code cannot exceed 7 characters")
    @JsonProperty("category_code")
    private String categoryCode;
}
