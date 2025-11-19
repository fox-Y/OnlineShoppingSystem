package org.example.onlineshoppingsystem.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item",
        uniqueConstraints = @UniqueConstraint(name = "uq_order_product", columnNames = {"order_id","product_id"}))
@Getter
@Setter
@ToString
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false) private Integer quantity;
    @Column(nullable = false) private BigDecimal purchasedPrice;
    @Column(nullable = false) private BigDecimal wholesalePrice;
}
