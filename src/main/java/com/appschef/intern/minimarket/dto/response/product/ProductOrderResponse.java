package com.appschef.intern.minimarket.dto.response.product;

import com.appschef.intern.minimarket.dto.response.promo.PromoOrderResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderResponse {
    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_price")
    private BigDecimal productPrice;

    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @JsonProperty("promo_list")
    private List<PromoOrderResponse> promoList;

    @JsonProperty("product_quantity")
    private Integer productQuantity;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;
}
