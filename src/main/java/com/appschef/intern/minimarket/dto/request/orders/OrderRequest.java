package com.appschef.intern.minimarket.dto.request.orders;

import com.appschef.intern.minimarket.dto.request.product.ProductOrderRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @Size(max = 7, message = "member_number cannot exceed 13 characters")
    @JsonProperty("nomor_member")
    private String nomorMember;

    @NotNull(message = "product_list must be provided")
    @JsonProperty("product_list")
    @Valid
    private List<ProductOrderRequest> orderProductList;

}
