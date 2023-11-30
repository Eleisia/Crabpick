package com.e107.backend.geChu.domain.repository;

import com.e107.backend.geChu.domain.entity.SteamLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SteamLibraryRepository extends JpaRepository<SteamLibrary, Long> {

    List<SteamLibrary> findByMemberId(Long memberId);
}
