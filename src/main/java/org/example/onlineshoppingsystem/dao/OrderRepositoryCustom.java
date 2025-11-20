package org.example.onlineshoppingsystem.dao;

import org.example.onlineshoppingsystem.domain.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    Order lockById(Long orderId);
    List<Long> recentProductIdsForUser(Long userId, int n);
    List<Object[]> frequentForUser(Long userId, int n);
}
