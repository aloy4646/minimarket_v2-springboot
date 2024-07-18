package com.appschef.intern.minimarket.dto.response.orders;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailySalesResponse {
    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @JsonProperty("gross_sales_total")
    private BigDecimal grossSalesTotal;

    @JsonProperty("total_discount_amount")
    private BigDecimal totalDiscountAmount;

    @JsonProperty("net_sales_total")
    private BigDecimal netSalesTotal;
}
