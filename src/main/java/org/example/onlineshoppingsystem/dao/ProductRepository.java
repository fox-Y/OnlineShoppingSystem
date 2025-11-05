package org.example.onlineshoppingsystem.dao;

import org.example.onlineshoppingsystem.dao.projection.ProductAdminView;
import org.example.onlineshoppingsystem.dao.projection.ProductListView;
import org.example.onlineshoppingsystem.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("SELECT p FROM Product p WHERE p.quantity > 0")
    Page<ProductListView> findCatalog(Pageable pageable);

    @Query("SELECT p FROM Product p")
    Page<ProductAdminView> findAllForAdmin(Pageable pageable);

    Page<ProductListView> findByNameContainingIgnoreCaseAndQuantityGreaterThan(String name, int minQty, Pageable pageable);
}
