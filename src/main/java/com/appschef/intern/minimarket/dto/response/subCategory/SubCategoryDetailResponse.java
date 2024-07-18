package com.appschef.intern.minimarket.dto.response.subCategory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDetailResponse {
    @JsonProperty("category_code")
    private String categoryCode;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("subcategory_code")
    private String subCategoryCode;

    @JsonProperty("subcategory_name")
    private String subCategoryName;
}
