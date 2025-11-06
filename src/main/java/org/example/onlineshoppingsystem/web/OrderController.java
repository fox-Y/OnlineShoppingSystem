package org.example.onlineshoppingsystem.web;

import org.example.onlineshoppingsystem.common.dto.IdRes;
import org.example.onlineshoppingsystem.common.dto.OrderReq;
import org.example.onlineshoppingsystem.domain.entity.Order;
import org.example.onlineshoppingsystem.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.example.onlineshoppingsystem.web.WebUtil.currentUserId;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    // POST /orders -- place order
    @PostMapping
    public IdRes place(@RequestBody OrderReq req, Authentication auth) {
        return orderService.place(currentUserId(auth), req);
    }

    // GET /orders/all -- list my orders
    @GetMapping("/all")
    public Page<Order> myOrders(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size,
                                Authentication auth) {
        return orderService.myOrders(currentUserId(auth), page, size);
    }

    // GET /orders/{id}
    @GetMapping("/{id}")
    public Order detail(@PathVariable Long id) {
        return orderService.detail(id);
    }

    // PATCH /orders/{id}/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return ResponseEntity.ok().build();
    }

    // PATCH /orders/{id}/complete
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long id) {
        orderService.complete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/all", params = "admin=true")
    public org.springframework.data.domain.Page<Order> allOrdersAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return orderService.allOrdersAdmin(page, size);
    }
}
