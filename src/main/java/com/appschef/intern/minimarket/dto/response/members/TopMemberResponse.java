package com.appschef.intern.minimarket.dto.response.members;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopMemberResponse {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("member_number")
    private String memberNumber;

    @JsonProperty("total_purchase_amount")
    private BigDecimal totalPurchaseAmount;
}
