package org.example.onlineshoppingsystem.service;

import org.example.onlineshoppingsystem.dao.ProductRepository;
import org.example.onlineshoppingsystem.dao.UserRepository;
import org.example.onlineshoppingsystem.dao.WatchlistRepository;
import org.example.onlineshoppingsystem.dao.projection.ProductListView;
import org.example.onlineshoppingsystem.domain.entity.Product;
import org.example.onlineshoppingsystem.domain.entity.User;
import org.example.onlineshoppingsystem.domain.entity.WatchId;
import org.example.onlineshoppingsystem.domain.entity.Watchlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public WatchlistService(WatchlistRepository watchRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.watchRepo = watchRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public Page<ProductListView> inStockProducts(Long userId, int page, int size) {

        List<Long> ids = watchRepo.findByUser_UserId(userId).stream()
                .map(w -> w.getProduct().getProductId())
                .distinct()
                .toList();
        if (ids.isEmpty()) {
            return Page.empty(PageRequest.of(page, size));
        }

        var all = productRepo.findAllById(ids).stream()
                .filter(p -> p.getQuantity() != null && p.getQuantity() > 0)
                .map(p -> (ProductListView) new ProductListView() {
                    public Long getProductId(){ return p.getProductId(); }
                    public String getName(){ return p.getName(); }
                    public String getDescription(){ return p.getDescription(); }
                    public java.math.BigDecimal getRetailPrice(){ return p.getRetailPrice(); }
                }).toList();

        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());

        return new PageImpl<>(all.subList(from, to), PageRequest.of(page, size), all.size());
    }

    @Transactional
    public void add(Long userId, Long productId) {
        if (watchRepo.existsByUser_UserIdAndProduct_ProductId(userId, productId)) {
            return;
        }

        var user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        var w = new Watchlist();
        w.setUser(user);
        w.setProduct(product);
        watchRepo.save(w);
    }

    @Transactional
    public void remove(Long userId, Long productId) {
        var id = new WatchId(userId, productId);
        watchRepo.findById(id).ifPresent(watchRepo::delete);
    }
}
