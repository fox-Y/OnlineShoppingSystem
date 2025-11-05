package org.example.onlineshoppingsystem.dao;

import org.example.onlineshoppingsystem.domain.entity.Order;
import org.example.onlineshoppingsystem.domain.enums.OrderStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Page<Order> findByUser_UserIdOrderByDatePlacedDesc(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findWithItemsByOrderId(Long orderId);

    long countByUser_UserIdAndOrderStatus(Long userId, OrderStatus status);
}
