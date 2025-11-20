package org.example.onlineshoppingsystem.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.onlineshoppingsystem.domain.enums.OrderStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.PROCESSING;

    @Column(nullable = false) private Instant datePlaced;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist void pre() {
        if (datePlaced == null) datePlaced = Instant.now();
        if (orderStatus == null) orderStatus = OrderStatus.PROCESSING;
    }
}
