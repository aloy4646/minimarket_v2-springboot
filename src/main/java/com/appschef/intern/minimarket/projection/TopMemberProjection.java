package com.appschef.intern.minimarket.projection;

import java.math.BigDecimal;

public interface TopMemberProjection {
    String getMemberNumber();
    String getFullName();
    BigDecimal getTotalPurchase();
}
