package org.example.onlineshoppingsystem.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.onlineshoppingsystem.domain.entity.OrderItem;
import org.example.onlineshoppingsystem.domain.entity.Product;
import org.example.onlineshoppingsystem.domain.enums.OrderStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void decrementStockWithLock(Long productId, int qty) {

        var p = em.find(Product.class, productId, LockModeType.PESSIMISTIC_WRITE);
        if (p == null) throw new EntityNotFoundException("Product " + productId + " not found");
        if (p.getQuantity() < qty) throw new IllegalStateException("Not enough inventory");
        p.setQuantity(p.getQuantity() - qty);
    }

    @Override
    public int batchAdjustRetailPrice(List<Long> productIds, double ratio) {

        if (productIds == null || productIds.isEmpty()) return 0;

        return em.createQuery("""
                UPDATE Product p
                SET p.retailPrice = p.retailPrice * :ratio
                WHERE p.productId IN :ids
                """)
                .setParameter("ratio", BigDecimal.valueOf(ratio))
                .setParameter("ids", productIds)
                .executeUpdate();
    }

    @Override
    public List<Object[]> popularTopNByCriteria(int n) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<OrderItem> oi = cq.from(OrderItem.class);
        var orderJoin = oi.join("order");
        var productJoin = oi.join("product");
        var sumQty = cb.sum(oi.<Number>get("quantity"));

        cq.multiselect(productJoin.get("productId"), sumQty)
                .where(cb.equal(orderJoin.get("orderStatus"), OrderStatus.COMPLETED))
                .groupBy(productJoin.get("productId"))
                .orderBy(cb.desc(sumQty));

        return em.createQuery(cq).setMaxResults(n).getResultList();
    }

    @Override
    public List<Object[]> mostProfitableTopN(int n) {

        return em.createQuery("""
                SELECT oi.product.productId,
                       SUM( (oi.purchasedPrice - oi.wholesalePrice) * oi.quantity )
                FROM OrderItem oi
                JOIN oi.order o
                WHERE o.orderStatus = :st
                GROUP BY oi.product.productId
                ORDER BY 2 DESC
                """, Object[].class)
                .setParameter("st", OrderStatus.COMPLETED)
                .setMaxResults(n)
                .getResultList();
    }
}
