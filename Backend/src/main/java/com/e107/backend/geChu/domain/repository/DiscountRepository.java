package com.e107.backend.geChu.domain.repository;

import com.e107.backend.geChu.domain.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount,Long> {

    boolean existsByGameId(long id);
}
