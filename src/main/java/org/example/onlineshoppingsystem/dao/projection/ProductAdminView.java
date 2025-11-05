package org.example.onlineshoppingsystem.dao.projection;

import java.math.BigDecimal;

public interface ProductAdminView {
    Long getProductId();
    String getName();
    String getDescription();
    Integer getQuantity();
    BigDecimal getRetailPrice();
    BigDecimal getWholesalePrice();
}
