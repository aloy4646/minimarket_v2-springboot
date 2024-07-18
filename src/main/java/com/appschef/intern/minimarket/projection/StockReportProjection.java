package com.appschef.intern.minimarket.projection;

public interface StockReportProjection {
    Long getTotalSales();
    Long getTotalExpired();
    Long getTotalAdditions();
}
