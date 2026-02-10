package com.rs.app.repositories;

import com.rs.app.domain.entities.Community;
import com.rs.app.domain.enums.PublicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CommunityRepo extends JpaRepository<Community, Long> {
    Page<Community> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Community> findByTitleContainingIgnoreCaseAndStatusIs(String title, PublicationStatus status, Pageable pageable);
    Page<Community> findByStatusIs(PublicationStatus status, Pageable pageable);
    Page<Community> findByGameId(Long gameId, Pageable pageable);
    Page<Community> findByGameIdAndStatusIs(Long gameId, PublicationStatus status, Pageable pageable);
    Optional<Community> findByIdAndStatusIs(Long id, PublicationStatus status);
}
