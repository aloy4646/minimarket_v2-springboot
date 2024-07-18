package com.appschef.intern.minimarket.dto.response.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReportResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("total_sales")
    private Long totalSales;

    @JsonProperty("total_expired")
    private Long totalExpired;

    @JsonProperty("total_additions")
    private Long totalAdditions;
}
