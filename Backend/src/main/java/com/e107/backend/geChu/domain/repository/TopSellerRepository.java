package com.e107.backend.geChu.domain.repository;

import com.e107.backend.geChu.domain.entity.TopSeller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopSellerRepository extends JpaRepository<TopSeller, Long> {
    boolean existsByGameId(long id);
}
