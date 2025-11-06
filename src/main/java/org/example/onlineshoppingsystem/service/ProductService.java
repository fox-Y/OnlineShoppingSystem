package org.example.onlineshoppingsystem.service;

import org.example.onlineshoppingsystem.common.dto.ProductDetailRes;
import org.example.onlineshoppingsystem.dao.ProductRepository;
import org.example.onlineshoppingsystem.dao.projection.ProductAdminView;
import org.example.onlineshoppingsystem.dao.projection.ProductListView;
import org.example.onlineshoppingsystem.domain.entity.Product;
import org.example.onlineshoppingsystem.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Transactional(readOnly = true)
    public Page<ProductListView> catalogForUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return productRepo.findCatalog(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductAdminView> catalogForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return productRepo.findAllForAdmin(pageable);
    }

    @Transactional(readOnly = true)
    public ProductDetailRes detail(Long id, Role role) {
        Product p = productRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (role == Role.ADMIN) {
            return ProductDetailRes.admin(
                    p.getProductId(), p.getName(), p.getDescription(),
                    p.getQuantity(), p.getRetailPrice(), p.getWholesalePrice()
            );
        } else {
            if (p.getQuantity() <= 0) throw new IllegalArgumentException("Product not available");
            return ProductDetailRes.user(
                    p.getProductId(), p.getName(), p.getDescription(), p.getRetailPrice()
            );
        }
    }

    @Transactional
    public Long create(ProductDetailRes.AdminUpsert req) {
        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setQuantity(req.getQuantity());
        p.setRetailPrice(req.getRetailPrice());
        p.setWholesalePrice(req.getWholesalePrice());
        return productRepo.save(p).getProductId();
    }

    @Transactional
    public void update(Long id, ProductDetailRes.AdminUpsert req) {
        Product p = productRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (req.getName() != null) p.setName(req.getName());
        if (req.getDescription() != null) p.setDescription(req.getDescription());
        if (req.getQuantity() != null) p.setQuantity(req.getQuantity());
        if (req.getRetailPrice() != null) p.setRetailPrice(req.getRetailPrice());
        if (req.getWholesalePrice() != null) p.setWholesalePrice(req.getWholesalePrice());
    }
}
