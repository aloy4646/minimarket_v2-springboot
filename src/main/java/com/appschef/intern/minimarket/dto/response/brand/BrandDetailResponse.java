package com.appschef.intern.minimarket.dto.response.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDetailResponse {
    @JsonProperty("brand_code")
    private String brandCode;

    @JsonProperty("brand_name")
    private String brandName;
}
