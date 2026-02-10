package com.rs.app.repositories;

import com.rs.app.domain.entities.Game;
import com.rs.app.domain.enums.PublicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepo extends JpaRepository<Game, Long> {
    Page<Game> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Game> findByTitleContainingIgnoreCaseAndStatusIs(String title, PublicationStatus status, Pageable pageable);
    Page<Game> findByStatusIs(PublicationStatus status, Pageable pageable);
}