package com.e107.backend.geChu.security.repository;

import com.e107.backend.geChu.security.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
