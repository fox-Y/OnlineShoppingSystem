package org.example.onlineshoppingsystem.dao;

import org.example.onlineshoppingsystem.domain.entity.WatchId;
import org.example.onlineshoppingsystem.domain.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, WatchId> {

    boolean existsByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

    List<Watchlist> findByUser_UserId(Long userId);
}
