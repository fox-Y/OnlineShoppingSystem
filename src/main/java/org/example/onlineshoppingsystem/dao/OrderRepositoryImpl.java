package org.example.onlineshoppingsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.onlineshoppingsystem.domain.entity.Order;
import org.springframework.stereotype.Repository;

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
}
