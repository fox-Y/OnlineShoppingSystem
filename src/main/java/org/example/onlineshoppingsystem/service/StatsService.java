package org.example.onlineshoppingsystem.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshoppingsystem.common.dto.PopularRes;
import org.example.onlineshoppingsystem.common.dto.ProfitRes;
import org.example.onlineshoppingsystem.dao.OrderRepository;
import org.example.onlineshoppingsystem.dao.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo; // ← add this

    // ----- admin/global stats -----

    @Transactional(readOnly = true)
    public List<PopularRes> popularTopN(int n) {
        return productRepo.popularTopNByCriteria(n).stream()
                .map(row -> new PopularRes(
                        ((Number) row[0]).longValue(),
                        ((Number) row[1]).longValue()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProfitRes> mostProfitableTopN(int n) {
        return productRepo.mostProfitableTopN(n).stream()
                .map(row -> new ProfitRes(
                        ((Number) row[0]).longValue(),
                        (row[1] == null) ? BigDecimal.ZERO : new BigDecimal(row[1].toString())
                ))
                .toList();
    }

    // ----- per-user stats (moved here from UserStatsService) -----

    @Transactional(readOnly = true)
    public List<Long> recentProductIdsForUser(Long userId, int n) {
        return orderRepo.recentProductIdsForUser(userId, n);
    }

    @Transactional(readOnly = true)
    public List<PopularRes> frequentForUser(Long userId, int n) {
        return orderRepo.frequentForUser(userId, n).stream()
                .map(row -> new PopularRes(
                        ((Number) row[0]).longValue(),
                        ((Number) row[1]).longValue()
                ))
                .toList();
    }
}
