package com.appschef.intern.minimarket.dto.response.promo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoOrderResponse {
    @JsonProperty("promo_code")
    private String promoCode;

    @JsonProperty("promo_name")
    private String promoName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("value")
    private BigDecimal value;
}
