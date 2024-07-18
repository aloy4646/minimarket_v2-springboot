package com.appschef.intern.minimarket.projection;

import java.math.BigDecimal;

public interface DailySalesProjection {
    BigDecimal getTotalGrossSales();
    BigDecimal getTotalNetSales();
}
