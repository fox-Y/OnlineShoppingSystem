package org.example.onlineshoppingsystem.dao;

import java.util.List;

public interface ProductRepositoryCustom {

    void decrementStockWithLock(Long productId, int qty);

    int batchAdjustRetailPrice(List<Long> productIds, double ratio);

    List<Object[]> popularTopNByCriteria(int n);

    List<Object[]> mostProfitableTopN(int n);
}
