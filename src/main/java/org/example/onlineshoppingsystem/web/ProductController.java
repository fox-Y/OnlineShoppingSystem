package org.example.onlineshoppingsystem.web;

import org.example.onlineshoppingsystem.auth.JwtUser;
import org.example.onlineshoppingsystem.common.dto.IdRes;
import org.example.onlineshoppingsystem.common.dto.PopularRes;
import org.example.onlineshoppingsystem.common.dto.ProductDetailRes;
import org.example.onlineshoppingsystem.common.dto.ProfitRes;
import org.example.onlineshoppingsystem.domain.enums.Role;
import org.example.onlineshoppingsystem.service.ProductService;
import org.example.onlineshoppingsystem.service.StatsService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.onlineshoppingsystem.web.WebUtil.currentUserId;
import static org.example.onlineshoppingsystem.web.WebUtil.isAdmin;

@RestController
@RequestMapping("/products")
@EnableMethodSecurity
public class ProductController {

    private final ProductService productService;
    private final StatsService statsService;

    public ProductController(ProductService productService, StatsService statsService) {
        this.productService = productService;
        this.statsService = statsService;
    }

    @GetMapping("/all")
    public Page<?> all(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     Authentication auth) {

        if (isAdmin(auth)) {
            return productService.catalogForAdmin(page, size);
        }

        return productService.catalogForUser(page, size);
    }

    @GetMapping("/{id}")
    public ProductDetailRes detail(@PathVariable Long id, Authentication auth) {
        var role = (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof JwtUser u
                && "ADMIN".equalsIgnoreCase(u.getRole()))
                ? Role.ADMIN : Role.USER;
        return productService.detail(id, role);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public IdRes create(@RequestBody ProductDetailRes.AdminUpsert req) {
        return new IdRes(productService.create(req));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void update(@PathVariable Long id, @RequestBody ProductDetailRes.AdminUpsert req) {
        productService.update(id, req);
    }

    @GetMapping("/popular/{n}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PopularRes> popular(@PathVariable int n) {
        return statsService.popularTopN(n);
    }

    @GetMapping("/profit/{n}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProfitRes> profit(@PathVariable int n) {
        return statsService.mostProfitableTopN(n);
    }

    @GetMapping("/recent/{n}")
    public List<Long> recent(@PathVariable int n, Authentication auth) {
        return statsService.recentProductIdsForUser(currentUserId(auth), n);
    }

    @GetMapping("/frequent/{n}")
    public List<PopularRes> frequent(@PathVariable int n, Authentication auth) {
        return statsService.frequentForUser(currentUserId(auth), n);
    }
}
