package com.appschef.intern.minimarket.dto.response.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XXStockReportResponse {
    @JsonProperty("category_code")
    private String categoryCode;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("subcategory_code")
    private String subCategoryCode;

    @JsonProperty("subcategory_name")
    private String subCategoryName;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("current_stock")
    private Integer current_stock;

    @JsonProperty("stock_report_detail")
    private List<StockReportResponse> stockReportDetail;
}
