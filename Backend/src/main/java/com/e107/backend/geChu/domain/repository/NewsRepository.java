package com.e107.backend.geChu.domain.repository;

import com.e107.backend.geChu.domain.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long > {
}
