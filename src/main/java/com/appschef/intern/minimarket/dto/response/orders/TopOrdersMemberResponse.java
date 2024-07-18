package com.appschef.intern.minimarket.dto.response.orders;

import com.appschef.intern.minimarket.dto.response.product.TopProductResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopOrdersMemberResponse {
    @JsonProperty("member_number")
    private String memberNumber;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("list_products")
    private List<TopProductResponse> productList;
}
