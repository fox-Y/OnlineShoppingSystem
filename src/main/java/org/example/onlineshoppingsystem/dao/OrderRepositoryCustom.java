package org.example.onlineshoppingsystem.dao;

import org.example.onlineshoppingsystem.domain.entity.Order;

public interface OrderRepositoryCustom {

    Order lockById(Long orderId);
}
