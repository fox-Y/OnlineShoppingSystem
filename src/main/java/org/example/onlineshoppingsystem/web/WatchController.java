package org.example.onlineshoppingsystem.web;

import io.swagger.v3.oas.annotations.Operation;
import org.example.onlineshoppingsystem.dao.projection.ProductListView;
import org.example.onlineshoppingsystem.service.WatchlistService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.example.onlineshoppingsystem.web.WebUtil.currentUserId;

@RestController
@RequestMapping("/watchlist")
public class WatchController {

    private final WatchlistService watchService;
    public WatchController(WatchlistService watchService) { this.watchService = watchService; }

    // GET /watchlist/products/all
    @GetMapping("/products/all")
    public Page<ProductListView> list(Authentication auth,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return watchService.inStockProducts(currentUserId(auth), page, size);
    }

    // POST /watchlist/product/{id}
    @PostMapping("/product/{productId}")
    public ResponseEntity<Void> add(@PathVariable Long productId, Authentication auth) {
        watchService.add(currentUserId(auth), productId);
        return ResponseEntity.ok().build();
    }

    // DELETE /watchlist/product/{id}
    @Operation(summary = "RemoveFromWatchlist")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long productId, Authentication auth) {
        watchService.remove(currentUserId(auth), productId);
        return ResponseEntity.ok().build();
    }
}
