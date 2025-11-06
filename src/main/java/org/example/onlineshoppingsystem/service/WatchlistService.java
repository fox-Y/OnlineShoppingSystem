package org.example.onlineshoppingsystem.service;

import org.example.onlineshoppingsystem.dao.ProductRepository;
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

    public WatchlistService(WatchlistRepository watchRepo, ProductRepository productRepo) {
        this.watchRepo = watchRepo;
        this.productRepo = productRepo;
    }

    @Transactional(readOnly = true)
    public Page<ProductListView> inStockProducts(Long userId, int page, int size) {
        // 1) 拿到用户的 productId 列表
        List<Long> ids = watchRepo.findByUser_UserId(userId)
                .stream().map(w -> w.getProduct().getProductId()).toList();
        if (ids.isEmpty()) {
            return Page.empty(PageRequest.of(page, size));
        }
        // 2) 过滤只看在库（用用户 catalog 查询 + name/ids 可扩展）
        // 这里简化为分页在 service 端做（实际可写一个 in-stock by ids 的自定义 repo 方法）
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
        if (watchRepo.existsByUser_UserIdAndProduct_ProductId(userId, productId)) return;
        var w = new Watchlist();
        w.setUser(new User()); w.getUser().setUserId(userId);
        w.setProduct(new Product()); w.getProduct().setProductId(productId);
        watchRepo.save(w);
    }

    @Transactional
    public void remove(Long userId, Long productId) {
        var id = new WatchId(userId, productId);
        watchRepo.findById(id).ifPresent(watchRepo::delete);
    }
}
