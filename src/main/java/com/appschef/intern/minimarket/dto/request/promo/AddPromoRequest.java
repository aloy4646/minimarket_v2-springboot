package com.appschef.intern.minimarket.dto.request.promo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPromoRequest {
    @NotBlank(message = "promo_code must be provided")
    @Size(max = 7, message = "promo_code cannot exceed 7 characters")
    @JsonProperty("promo_code")
    private String promoCode;

    @NotBlank(message = "promo_name must be provided")
    @Size(max = 100, message = "promo_name cannot exceed 100 characters")
    @JsonProperty("promo_name")
    private String promoName;

    @NotBlank(message = "product_code must be provided")
    @Size(max = 13, message = "product_code cannot exceed 13 characters")
    @JsonProperty("product_code")
    private String productCode;

    @NotBlank(message = "type must be provided")
    @JsonProperty("type")
    private String type;

    @NotNull(message = "value must be provided")
    @PositiveOrZero(message = "value must be greater than or equal to 0")
    @JsonProperty("value")
    private BigDecimal value;

    @NotBlank(message = "start_date must be provided")
    @JsonProperty("start_date")
    private String startDate;

    @NotBlank(message = "end_date must be provided")
    @JsonProperty("end_date")
    private String endDate;
}
