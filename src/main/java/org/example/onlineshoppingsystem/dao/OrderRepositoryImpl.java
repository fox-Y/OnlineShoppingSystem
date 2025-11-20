package org.example.onlineshoppingsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.onlineshoppingsystem.domain.entity.Order;
import org.example.onlineshoppingsystem.domain.enums.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Order lockById(Long orderId) {

        Order o = em.find(Order.class, orderId, LockModeType.PESSIMISTIC_WRITE);

        if (o == null) {
            throw new EntityNotFoundException("Order " + orderId + " not found");
        }

        return o;
    }

    @Override
    public List<Long> recentProductIdsForUser(Long userId, int n) {
        // fetch raw product ids ordered by recency
        List<Long> raw = em.createQuery("""
            SELECT oi.product.productId
            FROM OrderItem oi JOIN oi.order o
            WHERE o.user.userId = :uid AND o.orderStatus = :st
            ORDER BY o.datePlaced DESC, oi.itemId DESC
            """, Long.class)
                .setParameter("uid", userId)
                .setParameter("st", OrderStatus.COMPLETED)
                .setMaxResults(n * 5)
                .getResultList();

        // keep unique ids in order
        LinkedHashSet<Long> uniq = new LinkedHashSet<>();
        for (Long id : raw) {
            uniq.add(id);
            if (uniq.size() == n) {
                break;
            }
        }
        return new ArrayList<>(uniq);
    }

    @Override
    public List<Object[]> frequentForUser(Long userId, int n) {
        return em.createQuery("""
                SELECT oi.product.productId, SUM(oi.quantity)
                FROM OrderItem oi JOIN oi.order o
                WHERE o.user.userId = :uid AND o.orderStatus = :st
                GROUP BY oi.product.productId
                ORDER BY SUM(oi.quantity) DESC
                """, Object[].class)
                .setParameter("uid", userId)
                .setParameter("st", OrderStatus.COMPLETED)
                .setMaxResults(n)
                .getResultList();
    }
}
