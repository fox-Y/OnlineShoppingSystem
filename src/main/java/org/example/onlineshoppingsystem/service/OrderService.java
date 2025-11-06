package org.example.onlineshoppingsystem.service;

import org.example.onlineshoppingsystem.common.dto.IdRes;
import org.example.onlineshoppingsystem.common.dto.OrderReq;
import org.example.onlineshoppingsystem.dao.OrderRepository;
import org.example.onlineshoppingsystem.dao.ProductRepository;
import org.example.onlineshoppingsystem.dao.UserRepository;
import org.example.onlineshoppingsystem.domain.entity.Order;
import org.example.onlineshoppingsystem.domain.entity.OrderItem;
import org.example.onlineshoppingsystem.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    public OrderService(UserRepository userRepo,
                        ProductRepository productRepo,
                        OrderRepository orderRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    @Transactional
    public IdRes place(Long userId, OrderReq req) {
        var user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setDatePlaced(Instant.now());

        // 逐条扣库存（悲观锁），并固化历史价格
        req.getOrder().forEach(it -> {
            productRepo.decrementStockWithLock(it.getProductId(), it.getQuantity());
            var product = productRepo.findById(it.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(it.getQuantity());
            oi.setPurchasedPrice(product.getRetailPrice());
            oi.setWholesalePrice(product.getWholesalePrice());
            order.getItems().add(oi);
        });

        Long id = orderRepo.save(order).getOrderId();
        return new IdRes(id);
    }

    @Transactional(readOnly = true)
    public Page<Order> myOrders(Long userId, int page, int size) {
        return orderRepo.findByUser_UserIdOrderByDatePlacedDesc(
                userId, PageRequest.of(page, size)
        );
    }

    @Transactional(readOnly = true)
    public Order detail(Long orderId) {
        return orderRepo.findWithItemsByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public void cancel(Long orderId) {
        var o = orderRepo.lockById(orderId);
        if (o.getOrderStatus() == OrderStatus.COMPLETED) throw new IllegalStateException("Completed cannot cancel");
        if (o.getOrderStatus() == OrderStatus.CANCELED) return;
        o.setOrderStatus(OrderStatus.CANCELED);
        // 回补库存：传负数或写单独方法
        o.getItems().forEach(oi ->
                productRepo.decrementStockWithLock(oi.getProduct().getProductId(), -oi.getQuantity())
        );
    }

    @Transactional
    public void complete(Long orderId) {
        var o = orderRepo.lockById(orderId);
        if (o.getOrderStatus() == OrderStatus.CANCELED) throw new IllegalStateException("Canceled cannot complete");
        o.setOrderStatus(OrderStatus.COMPLETED);
    }
}
