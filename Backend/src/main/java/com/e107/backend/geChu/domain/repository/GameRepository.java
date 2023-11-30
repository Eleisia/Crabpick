package com.e107.backend.geChu.domain.repository;

import com.e107.backend.geChu.domain.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByAppId(Long appId);
    Page<Game> findAll(Pageable pageable);
    Page<Game> findByNameContaining(String name, Pageable pageable);

}
