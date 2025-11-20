package org.example.onlineshoppingsystem.dao.projection;

import java.math.BigDecimal;

public interface ProductListView {
    Long getProductId();
    String getName();
    String getDescription();
    BigDecimal getRetailPrice();
}
