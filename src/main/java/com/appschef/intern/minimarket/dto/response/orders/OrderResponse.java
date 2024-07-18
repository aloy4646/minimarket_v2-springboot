package com.appschef.intern.minimarket.dto.response.orders;

import com.appschef.intern.minimarket.dto.response.product.ProductOrderResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    @JsonProperty("member_number")
    private String memberNumber;

    @JsonProperty("receipt_number")
    private String receiptNumber;

    @JsonProperty("product_list")
    private List<ProductOrderResponse> productList;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;
}
