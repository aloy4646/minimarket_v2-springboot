package com.appschef.intern.minimarket.dto.response.promo;

import com.appschef.intern.minimarket.dto.response.members.TopMemberResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoReportResponse {
    @JsonProperty("promo_code")
    private String promoCode;

    @JsonProperty("promo_name")
    private String promoName;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("usage_count")
    private Long usageCount;

    @JsonProperty("top_using_members")
    private List<TopMemberResponse> topUsingMembers;
}
